package com.nines.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.nines.sys.entity.SysUser;
import com.nines.sys.service.ISysUserService;
import com.nines.sys.vo.DataPageVo;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
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
@RestController
@Api(description = "后台用户接口")
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private ISysUserService userService;

    @ApiOperation(value = "登录信息", notes = "获取登录者信息")
    @GetMapping("/login_info")
    public ResponseVo getUserInfo(){
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();
        if (user == null){
            return ResponseVo.error("未登录");
        }
        // 返回用户信息
        return ResponseVo.ok(user);
    }

    @ApiOperation(value = "获取用户信息", notes = "通过用户ID获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo getOneById(@PathVariable String id){
        SysUser user = userService.getOneById(id);
        if (user == null){
            return ResponseVo.fail("无效的ID");
        }
        return ResponseVo.ok(user);
    }

    @ApiOperation(value = "用户分页", notes = "用户列表分页")
    @ApiImplicitParam(name = "dataPageVo", value = "分页参数实体", dataType = "DataPageVo")
    @PostMapping("/data_page")
    public ResponseVo getDataPage(@RequestBody DataPageVo dataPageVo){
        return ResponseVo.ok(userService.getDataPage(dataPageVo));
    }

    @ApiOperation(value = "新增或修改数据", notes = "根据ID判断新增或修改数据")
    @PostMapping("/modif")
    public ResponseVo addOrUpdate(@RequestBody SysUser user){
        int result;
        if (StrUtil.hasBlank(user.getId())){
            result = userService.add(user);
        }else {
            result = userService.update(user);
        }
        if (result > 0){
            return ResponseVo.ok("操作成功");
        }
        return ResponseVo.fail("操作失败");
    }

    @ApiOperation(value = "删除用户", notes = "根据ID删除数据")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String")
    @PostMapping("/delete/{id}")
    public ResponseVo delete(@PathVariable String id){
        int result = userService.delete(id);
        if (result > 0){
            return ResponseVo.ok("操作成功");
        }
        return ResponseVo.fail("操作失败");
    }
}
