package com.nines.sys.controller;


import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.vo.ResponseVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-22
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private ISysUserService userService;

    @GetMapping("/login_info")
    public ResponseVo getUserInfo(){
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();
        if (user == null){
            return ResponseVo.error("未登录");
        }
        // 返回用户id和昵称
        return ResponseVo.ok(new HashMap<String, String>(2){
            {
                put("id", user.getId());
                put("nickName", user.getNickName());
            }
        });
    }

    @GetMapping("/info")
    public ResponseVo getInfo(){
        return ResponseVo.ok("123");
    }

}
