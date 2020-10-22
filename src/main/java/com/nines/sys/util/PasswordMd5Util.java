package com.nines.sys.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 密码进行MD5加密
 * </p>
 *
 * @author Nines
 * @since 2019-11-25
 */
public class PasswordMd5Util {

    /**
     * 加密算法 md5
     */
    private final static String ENCRYPTION_ALGORITHM = "md5";

    /**
     * 加密次数
     */
    private final static int TIME = 2;

    /**
     *  加密 密码
     */
    public static Map<String, String> encryption(String password){
        //随机出来的盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 加密
        String encryptPassword = new SimpleHash(ENCRYPTION_ALGORITHM, password, salt, TIME).toString();
        HashMap<String, String> map = new HashMap<>(2);
        map.put("encryptPassword", encryptPassword);
        map.put("salt", salt);
        return map;
    }

    /**
     * 对比加密
     * @param password 密码
     * @param salt 盐
     * @param encryptPassword 加密后数据
     * @return 是否
     */
    public static boolean comparePassword(String password, String salt, String encryptPassword){
        return encryptPassword.equals(new SimpleHash(ENCRYPTION_ALGORITHM, password, salt, TIME).toString());
    }

    public static void main(String[] args){
        Map<String, String> map = encryption("root");
        String encryptPassword = map.get("encryptPassword");
        String salt = map.get("salt");
        System.out.println("encryptPassword -> " + encryptPassword);
        System.out.println("salt -> " + salt);
        System.out.println(comparePassword("roots", salt, encryptPassword));
    }

}
