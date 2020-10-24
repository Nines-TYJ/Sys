package com.nines.sys.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 账号密码登录
 * @author TYJ
 * @date 2020/10/22 17:29
 */
@Data
public class AccountPasswordVo implements Serializable {

    private String username;

    private String password;

    private String code;

}
