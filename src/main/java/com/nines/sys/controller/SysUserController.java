package com.nines.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.util.Constant;
import com.nines.sys.util.RedisUtil;
import com.nines.sys.util.PageUtil;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-22
 */
@RequiresAuthentication
@RestController
@Api("后台用户相关接口")
@RequestMapping("/sys/user")
public class SysUserController {

    private static final String PREFIX_SHIRO_TOKEN = Constant.PREFIX_SHIRO_TOKEN;

    @Resource
    private ISysUserService userService;

    @RequiresPermissions({"user:select"})
    @ApiOperation(value = "登录信息", notes = "获取登录者信息")
    @GetMapping("/login_info")
    public ResponseVo getUserInfo(){
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();
        // 返回用户信息
        return user == null ? ResponseVo.error("未登录") : ResponseVo.ok(user);
    }

    @RequiresPermissions({"user:select"})
    @ApiOperation(value = "获取用户信息", notes = "通过用户ID获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo getOneById(@PathVariable String id){
        SysUser user = userService.findOneById(id);
        return user == null ? ResponseVo.fail("无效的ID") : ResponseVo.ok(user);
    }

    @RequiresPermissions({"user:select"})
    @ApiOperation(value = "用户分页", notes = "用户列表分页")
    @ApiImplicitParam(name = "pageVo", value = "分页参数实体", dataType = "PageVo")
    @PostMapping("/data_page")
    public ResponseVo getDataPage(@RequestBody PageUtil pageUtil){
        return ResponseVo.ok(userService.findPage(pageUtil));
    }

    @RequiresPermissions({"user:insert", "user:update"})
    @ApiOperation(value = "新增或修改数据", notes = "根据ID判断新增或修改数据")
    @PostMapping("/modify")
    public ResponseVo addOrUpdate(@RequestBody SysUser user){
        boolean result;
        if (StrUtil.hasBlank(user.getId())){
            result = userService.addUser(user);
        }else {
            result = userService.updateUser(user);
        }
        return result ? ResponseVo.ok("操作成功") : ResponseVo.fail("操作失败");
    }

    @RequiresPermissions({"user:delete"})
    @ApiOperation(value = "删除用户", notes = "根据ID删除数据")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String")
    @PostMapping("/delete/{id}")
    public ResponseVo delete(@PathVariable String id){
        return userService.deleteUserById(id) ? ResponseVo.ok("操作成功") : ResponseVo.fail("操作失败");
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @GetMapping("/logout")
    public ResponseVo logout(){
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();
        if (user == null){
            return ResponseVo.fail("用户未登录");
        }
        if (!RedisUtil.hasKey(PREFIX_SHIRO_TOKEN + user.getUserName())){
            return ResponseVo.fail("用户未登录");
        }
        // 删除缓存中的记录
        RedisUtil.del(PREFIX_SHIRO_TOKEN + user.getUserName());
        subject.logout();
        return ResponseVo.ok("退出成功");
    }
}
