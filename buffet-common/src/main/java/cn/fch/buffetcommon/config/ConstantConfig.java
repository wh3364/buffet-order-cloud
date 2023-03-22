package cn.fch.buffetcommon.config;

/**
 * @author fch
 * @program BuffetOrderCloud
 * @description 常量配置
 * @create 2023-03-21 10:33
 **/
public interface ConstantConfig {
    String WX_APP_ID = "";
    String WX_APP_SECRET = "";
    String WX_GRANT_TYPE = "";


    // SECRET 数字签名密钥
    String JWT_SECRET = "";
    // 有效期
    long JWT_EXPIRE = 1;
    // 还有多少到期时刷新
    long JWT_EXPECT = 1;


    String REDIS_HOST = "localhost:6379";
    Long DEFAULT_EXPIRED = 1000 * 60 * 60L;
}