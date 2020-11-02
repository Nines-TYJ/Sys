package com.nines.sys.util;

/**
 * 常量类
 * @author TYJ
 * @date 2020/10/30 10:09
 */
public class Constant {

    /**
     * 登录标识
     */
    public static final String LOGIN_SIGN = "Authorization";

    /**
     * token 有效时间（分钟）
     */
    public static final long JWT_MINUTE = 30;

    /**
     * 密码加密盐
     */
    public static final String JWT_SECRET = "NINES";

    /**
     * shiro 权限缓存前缀
     */
    public final static String FREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * 权限缓存 有效时间（分钟）
     */
    public static final long SHIRO_CACHE_EXPIRE_TIME = 15;

    /**
     * shiro token缓存前缀
     */
    public static final String PREFIX_SHIRO_TOKEN = "shiro:token:";

    /**
     * token缓存 有效时间（分钟）
     */
    public static final long SHIRO_TOKEN_EXPIRE_TIME = 60 * 24;

}
