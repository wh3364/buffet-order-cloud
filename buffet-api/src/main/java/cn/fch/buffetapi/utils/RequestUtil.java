package cn.fch.buffetapi.utils;

import cn.fch.buffetcommon.utils.RedissonUtils;
import cn.fch.buffetentity.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @program: BuffetOrder
 * @description: 请求工具
 * @CreatedBy: fch
 * @create: 2023-02-17 13:19
 **/
public class RequestUtil {

    private static final String BUCKET_NAME = "buffet-order";

    private static final String uploadImgUrl = "http://192.168.23.128:8080/file/upload";
//  private static final RequestUtil INSTANCE = new RequestUtil();


//    @PostConstruct
//    public void init(){
//        INSTANCE.APP_ID = APP_ID;
//        INSTANCE.APP_SECRET = APP_SECRET;
//        INSTANCE.GRANT_TYPE = GRANT_TYPE;
//    }

    public static String uploadImg(MultipartFile file, String name, String path) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return name;
                }
            };
            params.add("file", resource);
            params.add("bucketName", BUCKET_NAME);
            params.add("path", path);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);
            return restTemplate.postForObject(uploadImgUrl, entity, String.class);
        } catch (IOException | RestClientException e) {
            e.printStackTrace();
            return "上传失败";
        }
    }

    public static JSONObject getOpenId(String code, String APP_ID, String APP_SECRET, String GRANT_TYPE) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APP_ID + "&secret=" + APP_SECRET + "&js_code=" + code + "&grant_type=" + GRANT_TYPE;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        JSONObject res = new JSONObject(4);
        if (responseEntity.getStatusCodeValue() <= 200) {
            if (StringUtils.hasText(JSONObject.parseObject(responseEntity.getBody()).getString("errmsg"))) {
                res.put("flag", false);
                res.put("openId", "");
                res.put("msg", JSONObject.parseObject(responseEntity.getBody()).getString("errmsg"));
            } else {
                // 成功获取openId
                res.put("flag", true);
                res.put("session_key", JSONObject.parseObject(responseEntity.getBody()).getString("session_key"));
                res.put("openId", JSONObject.parseObject(responseEntity.getBody()).getString("openid"));
                res.put("msg", "成功获得openId");
            }
            return res;
        }
        res.put("flag", false);
        res.put("msg", "访问微信服务器失败");
        return res;
    }

    public static String getOpenIdFromSession(String sessionKey) {
        User user = (User) RedissonUtils.getStr("buffetorder:user:" + sessionKey);
        if (Objects.isNull(user)){
            return "";
        }
        return user.getOpenId();
    }
}
