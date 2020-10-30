package com.nines.sys.config.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName: JWTToken
 * @Description: 创建 JWTToken 替换 Shiro 原生 Token
 *                1. Shiro 原生的 Token 中存在用户名和密码以及其他信息 [验证码，记住我]
 *                2. 在 JWT 的 Token 中因为已将用户名和密码通过加密处理整合到一个加密串中，所以只需要一个 token 字段即可
 * @author: Nines
 * @date: 2019年12月03日 19:31
 */
public class JWTToken implements AuthenticationToken {

    /**
     * 密钥
     */
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
