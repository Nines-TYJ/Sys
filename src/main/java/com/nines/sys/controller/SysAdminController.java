package com.nines.sys.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.StrUtil;
import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.util.Constant;
import com.nines.sys.util.JWTUtil;
import com.nines.sys.util.RedisUtil;
import com.nines.sys.vo.AccountPasswordVo;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author TYJ
 * @date 2020/10/22 11:38
 */
@Slf4j
@RestController
@RequestMapping("/sys/admin")
@Api(description = "后台登录注册接口")
public class SysAdminController {

    @Resource
    private ISysUserService userService;


    /**
     * 登录标识
     */
    private static final String LOGIN_SIGN = Constant.LOGIN_SIGN;

    /**
     * shiro token缓存前缀
     */
    private static final String PREFIX_SHIRO_TOKEN = Constant.PREFIX_SHIRO_TOKEN;

    /**
     * token缓存 有效时间（分钟）
     */
    private static final long SHIRO_TOKEN_EXPIRE_TIME = 1000 * 60 * Constant.SHIRO_TOKEN_EXPIRE_TIME;

    /**
     * 判断用户名是否已存在
     * @param username 用户名
     * @return 是否
     */
    private boolean isExistsUsername(String username){
        return userService.getUserByUsername(username) != null;
    }

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在")
    @ApiImplicitParam(name = "username", value = "用户名", dataType = "String")
    @GetMapping("/exists/{username}")
    public ResponseVo existsUsername(@PathVariable String username){
        if (StrUtil.hasBlank(username)){
            return ResponseVo.fail("用户名不能为空");
        }
        if (isExistsUsername(username)){
            return ResponseVo.fail("用户名已存在");
        }
        return ResponseVo.ok("用户名可用");
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", produces = "application/json")
    @ApiImplicitParam(name = "user", value = "用户", dataType = "SysUser")
    @PostMapping("/register")
    public ResponseVo register(@RequestBody SysUser user){
        if (StrUtil.hasBlank(user.getUserName()) || StrUtil.hasBlank(user.getPassWord())){
            return ResponseVo.fail("用户名和密码不能为空");
        }
        if (isExistsUsername(user.getUserName())){
            return ResponseVo.fail("用户名已存在");
        }
        return userService.addUser(user) ? ResponseVo.ok("注册成功") : ResponseVo.error("注册失败");
    }

    @ApiOperation(value = "验证码", notes = "获取验证码图片")
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(300, 100, 5, 4);
        // session 缓存验证码
        request.getSession().setAttribute("CAPTCHA_KEY", captcha.getCode());
        // 验证码时效
        request.getSession().setAttribute("CAPTCHA_TIME", System.currentTimeMillis() + 1000 * 60 * 10);
        //告诉浏览器输出内容为图片
        response.setContentType("image/png");
        //禁止浏览器缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        // 将图片输出给浏览器
        captcha.write(response.getOutputStream());
        log.info("验证码结果：{}", captcha.getCode());
        // 写出到浏览器（Servlet输出）
        OutputStream os = response.getOutputStream();
        captcha.write(os);
        os.close();
    }

    @ApiOperation(value = "验证", notes = "验证码验证")
    @ApiImplicitParam(name = "code", value = "验证码", dataType = "String")
    @GetMapping("/verify/{code}")
    public ResponseVo verifyCaptcha(HttpServletRequest request, @PathVariable String code){
        if (request.getSession().getAttribute("CAPTCHA_KEY") == null){
            return ResponseVo.fail("请先获取验证码");
        }
        long captchaTime = (long) request.getSession().getAttribute("CAPTCHA_TIME");
        if (System.currentTimeMillis() > captchaTime){
            return ResponseVo.fail("验证码已过期");
        }
        String rightCode = (String) request.getSession().getAttribute("CAPTCHA_KEY");
        if (rightCode.equals(code)){
            return ResponseVo.ok("验证码通过");
        }
        return ResponseVo.fail("验证码错误");
    }

    @ApiOperation(value = "登录", notes = "账号密码登录")
    @ApiImplicitParam(name = "accountPasswordVo", value = "登录验证类", dataType = "AccountPasswordVo")
    @PostMapping("/account_login")
    public ResponseVo accountPasswordLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody AccountPasswordVo accountPasswordVo){
        if (StrUtil.hasBlank(accountPasswordVo.getUsername()) || StrUtil.hasBlank(accountPasswordVo.getPassword())){
            return ResponseVo.fail("用户名和密码不能为空");
        }
        if (request.getSession().getAttribute("CAPTCHA_KEY") == null){
            return ResponseVo.fail("请先获取验证码");
        }
        if (StrUtil.hasBlank(accountPasswordVo.getCode())){
            return ResponseVo.fail("验证码不能为空");
        }
        long captchaTime = (long) request.getSession().getAttribute("CAPTCHA_TIME");
        if (System.currentTimeMillis() > captchaTime){
            return ResponseVo.fail("验证码已过期");
        }
        String rightCode = (String) request.getSession().getAttribute("CAPTCHA_KEY");
        if (!rightCode.equals(accountPasswordVo.getCode())){
            return ResponseVo.fail("验证码错误");
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(accountPasswordVo.getUsername(), accountPasswordVo.getPassword());
        subject.login(token);
        SysUser user = (SysUser) subject.getPrincipal();
        log.info(user.toString());
        Long current = System.currentTimeMillis();
        // 生产token
        String jwtToken = JWTUtil.sign(user.getUserName(), user.getPassWord(), current);
        // refreshToken加入redis 1天内有效
        RedisUtil.set(PREFIX_SHIRO_TOKEN + user.getUserName(), current, SHIRO_TOKEN_EXPIRE_TIME);
        response.setHeader(LOGIN_SIGN, jwtToken);
        response.setHeader("Access-Control-Expose-Headers", LOGIN_SIGN);
        return ResponseVo.ok("登录成功");
    }
}
