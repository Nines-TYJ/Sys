package com.nines.sys.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author TYJ
 * @date 2020/10/21 14:43
 */
@Component
@Slf4j
public class JWTUtil {

    private static long EXPIRE_TIME = 1000 * 60 * Constant.JWT_MINUTE;

    private static String SECRET = Constant.JWT_SECRET;

    /**
     * 生成签名
     * @param username 用户名
     * @param password 密码
     * @return 签名
     */
    public static String sign(String username, String password, Long current) {
        // 指定过期时间
        Date date = new Date(current + EXPIRE_TIME);
        // 给加密算法传递加密盐
        Algorithm algorithm = Algorithm.HMAC256(password + SECRET);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                // 当前时间戳
                .withClaim("current", current)
                //到期时间
                .withExpiresAt(date)
                // 签发时间
                .withIssuedAt(new Date())
                //创建一个新的JWT，并使用给定的算法进行标记
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     *
     * @param token    密钥
//     * @param username 登录名
     * @param password 密码
     * @return boolean
     */
    public static boolean verify(String token, String password) {
        try {
            // 给加密算法传递加密盐
            Algorithm algorithm = Algorithm.HMAC256(password + SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @param token 密钥
     * @return 用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取token创建时间
     * @param token 密钥
     * @return 创建的时间戳
     */
    public static Long getExpire(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("current").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
