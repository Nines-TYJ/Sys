package com.nines.sys.controller;


import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.shiro.JWTFilter;
import com.nines.sys.util.JWTUtil;
import com.nines.sys.vo.ResponseVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public ResponseVo getUserInfo(HttpServletRequest request){
        // 获取token
        String token = request.getHeader(JWTFilter.LOGIN_SIGN);
        // 获取token中的username
        String username = JWTUtil.getUsername(token);
        // 通过username获取用户信息
        SysUser user = userService.getUserByUsername(username);
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
