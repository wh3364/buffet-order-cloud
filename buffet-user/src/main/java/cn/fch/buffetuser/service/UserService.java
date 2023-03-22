package cn.fch.buffetuser.service;

import cn.fch.buffetentity.entity.Address;
import cn.fch.buffetentity.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface UserService {

    User addMoney(String sessionKey, User user, BigDecimal money);

    boolean regUser(User user);

    Address getAddressByUserId(User user);

    boolean addOrUploadAddress(User user, Address newAddress);

    boolean uploadUserNick(String sessionKey, User user);

    boolean uploadUserAvatar(String sessionKey, User user, MultipartFile file);

    boolean isExistByOpenId(User user);

    User getUserByOpenId(String key, User user);

    User queryUserIdByOpenId(User user);

//    String queryUserNickByOpenId();
//
//    String queryUserAvatarByOpenId();
//
//    String WeiXinLogin(String code);

    ResponseEntity<User> uploadUserNickAvatar(String sessionKey, User user);
}