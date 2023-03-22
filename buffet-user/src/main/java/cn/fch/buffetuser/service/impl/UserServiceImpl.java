package cn.fch.buffetuser.service.impl;

import cn.fch.buffetapi.utils.RequestUtil;
import cn.fch.buffetcommon.aspect.AfterClearCache;
import cn.fch.buffetcommon.aspect.Cache;
import cn.fch.buffetentity.entity.Address;
import cn.fch.buffetentity.entity.User;
import cn.fch.buffetuser.dao.AddressDao;
import cn.fch.buffetuser.dao.UserDao;
import cn.fch.buffetuser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final AddressDao addressDao;

    @Override
    @AfterClearCache(fromArg = true, keyIndex = 0)
    public User addMoney(String sessionKey, User user, BigDecimal money) {
        User u = userDao.queryUserByOpenId(user);
        u.setMoney(u.getMoney().add(money));
        return userDao.uploadUserMoney(u) > 0 ? u : null;
    }

    /**
     * 首次登录,注册信息到数据库
     *
     * @param user
     * @return
     */
    @Override
    public boolean regUser(User user) {
        //微信用户默认姓名
        user.setNickName("微信用户");
        //微信用户默认头像
        user.setAvatarPath("https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0");
        return userDao.addUser(user) > 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Address getAddressByUserId(User user) {
        user = userDao.queryUserIdByOpenId(user);
        Address address = new Address();
        address.setUserId(user.getUserId());
        address = addressDao.queryAddressByUserId(address);
        return address;
    }

    @Override
    public boolean addOrUploadAddress(User user, Address newAddress) {
        user = userDao.queryUserIdByOpenId(user);
        newAddress.setUserId(user.getUserId());
        Address ad = addressDao.queryAddressByUserId(newAddress);
        if (ad == null) {
            addressDao.addAddress(newAddress);
            return true;
        } else {
            addressDao.uploadAddress(newAddress);
            return true;
        }
    }

    /**
     * 个系用户昵称
     *
     *
     * @param sessionKey
     * @param user
     * @return
     */
    @Override
    @AfterClearCache(fromArg = true, keyIndex = 0)
    public boolean uploadUserNick(String sessionKey, User user) {
        return userDao.uploadUserNick(user) > 0;
    }

    /**
     * 更新用户头像
     *
     *
     * @param sessionKey
     * @param user
     * @return
     */
    @Override
    @AfterClearCache(fromArg = true, keyIndex = 0)
    public boolean uploadUserAvatar(String sessionKey, User user, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String objectName = UUID.randomUUID().toString().replaceAll("-", "")
                + fileName.substring(fileName.lastIndexOf("."));
        String url = RequestUtil.uploadImg(file, objectName, "img/avatar/");
        if ("上传失败".equals(url)){
            return false;
        }else {
            user.setAvatarPath(url);
            return userDao.uploadUserAvatar(user) > 0;
        }
    }

    /**
     * 用户是否存在
     *
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public boolean isExistByOpenId(User user) {
        try {
            User u = userDao.queryUserByOpenId(user);
            if (u != null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cache(keyIndex = 0)
    public User getUserByOpenId(String key, User user) {
        return userDao.queryUserByOpenId(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User queryUserIdByOpenId(User user) {
        return userDao.queryUserIdByOpenId(user);
    }

    @Override
    @AfterClearCache(fromArg = true, keyIndex = 0)
    public ResponseEntity<User> uploadUserNickAvatar(String sessionKey, User user) {
        return userDao.uploadUserNick(user) + userDao.uploadUserAvatar(user) > 1 ? ResponseEntity.ok(user) : new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
