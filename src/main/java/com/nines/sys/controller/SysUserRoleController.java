package com.nines.sys.controller;


import com.nines.sys.entity.SysUserRole;
import com.nines.sys.service.ISysUserRoleService;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户与角色关系表 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@RestController
@RequestMapping("/sys/user_role")
public class SysUserRoleController {

    @Resource
    private ISysUserRoleService userRoleService;

    @ApiOperation(value = "获取用户角色列表", notes = "根据UserId获取用户角色列表")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo findListByUserId(@PathVariable String id){
        List<SysUserRole> userRoleList = userRoleService.findListByUserId(id);
        return ResponseVo.ok(userRoleList);
    }

    @ApiOperation(value = "修改用户角色列表", notes = "修改用户角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "userRoleList", value = "用户角色列表", dataType = "List<SysUserRole>")
    })
    @PostMapping("/modify/{id}")
    public ResponseVo modifyUserRole(@PathVariable String id, @RequestBody List<SysUserRole> userRoleList){
        return userRoleService.modifyUserRole(id, userRoleList) ? ResponseVo.ok("分配成功") : ResponseVo.fail("分配失败");
    }

}
