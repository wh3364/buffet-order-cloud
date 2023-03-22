/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.fch.buffetcommon.utils;

import cn.fch.buffetcommon.config.ConstantConfig;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author chnewei
 */
public class JwtUtils {

    //SECRET 数字签名密钥
    private static final String SECRET = ConstantConfig.JWT_SECRET;
    //EXPIRE 7天有效期
    private static final long EXPIRE = ConstantConfig.JWT_EXPIRE;
    //EXPECT 还有最后一天到期时刷新
    private static final long EXPECT = ConstantConfig.JWT_EXPECT;

    /**
     * 解析token
     *
     * @param token 需要解析的
     * @return Claims
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            //过期等token无效
            claims = null;
        }
        return claims;
    }

    /**
     * 生成 jwt token
     *
     * @param userName
     * @param role
     * @return
     */
    public static String generatorToken(String userName, String role) {
        //生成token
        return Jwts.builder()
                .setSubject(userName) //填写令牌的用户名
                .claim("role", role) //填写角色
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE)) //有效日期
                .signWith(SignatureAlgorithm.HS512, SECRET) //数字签名
                .compact();
    }
    /**
     * 生成 jwt token
     *
     * @param username 用户名
     * @param info 额外信息
     * @return String token
     */
    public static String generatorToken(String username, Map<String, Object> info) {
        //生成token
        return Jwts.builder()
                .setSubject(username) //填写令牌的用户名
                .addClaims(info)//填写角色
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE)) //有效日期
                .signWith(SignatureAlgorithm.HS512, SECRET) //数字签名
                .compact();
    }

    /**
     * 判断 token 是否要刷新
     *
     * @param token token
     * @return true:该刷新了
     */
    public static boolean needRefresh(Claims token) {
        return token.getExpiration().before(new Date(System.currentTimeMillis() + EXPECT));
    }
}
