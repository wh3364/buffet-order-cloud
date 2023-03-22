package cn.fch.buffetuser.interceptor;

import cn.fch.buffetapi.utils.RequestUtil;
import cn.fch.buffetcommon.utils.RedissonUtils;
import cn.fch.buffetcommon.utils.ThreadLocalUtils;
import cn.fch.buffetentity.entity.User;
import cn.fch.buffetuser.service.UserService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @program: BuffetOrder
 * @description:
 * @CreatedBy: fch
 * @create: 2022-10-25 17:42
 **/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${wx.app-id}")
    String APP_ID;
    @Value("${wx.app-secret}")
    String APP_SECRET;
    @Value("${wx.grant-type}")
    String GRANT_TYPE;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setContentType("application/json;charset=UTF-8");
        String session_key = request.getHeader("session_key");
        String code = request.getHeader("code");
        if (StringUtils.hasText(session_key)) {
            String openId = RequestUtil.getOpenIdFromSession(session_key);
            if (StringUtils.hasText(openId)) {
                request.setAttribute("openId", openId);
                response.setHeader("session_key", session_key);
                log.info("通过session_key登录 session_key:{}", session_key);
                return true;
            } else {
                response.setStatus(401);
                return false;
            }

        }
        if (StringUtils.hasText(code)) {
            JSONObject openIdres = RequestUtil.getOpenId(code, APP_ID, APP_SECRET, GRANT_TYPE);
            if (openIdres.getBoolean("flag")) {
                if (needReg(openIdres.getString("openId"))){
                    response.setStatus(403);
                    return false;
                }
                request.setAttribute("openId", openIdres.getString("openId"));
                response.setHeader("session_key", openIdres.getString("session_key"));
                log.info("通过code登录 code:{}", code);
                User user = new User();
                user.setOpenId(openIdres.getString("openId"));
                userService.getUserByOpenId("user:" + openIdres.getString("session_key"), user);
                return true;
            } else {
                response.setStatus(408);
                try (PrintWriter writer = response.getWriter()) {
                    writer.write(openIdres.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        response.setStatus(400);
        return false;
    }

    private boolean needReg(String openId){
        User user = new User();
        user.setOpenId(openId);
        if (Optional.ofNullable((Boolean) RedissonUtils.getStr("buffetorder:temp:" + openId)).orElse(false))
            return true;
        if (!userService.isExistByOpenId(user)){
            RedissonUtils.setStr("buffetorder:temp:" + openId, true, 1000L);
            return true;
        }else
            return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        ThreadLocalUtils.removeResources();
    }
}
