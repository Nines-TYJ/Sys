package com.nines.sys.controller;


import com.nines.sys.entity.SysRolePermission;
import com.nines.sys.service.ISysRolePermissionService;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色与权限关系表 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@RequiresAuthentication
@RestController
@RequestMapping("/sys/role_permission")
@Api(description = "后台角色权限相关接口")
public class SysRolePermissionController {

    @Resource
    private ISysRolePermissionService rolePermissionService;

    @RequiresPermissions({"role:select"})
    @ApiOperation(value = "获取角色权限列表", notes = "根据UserId获取角色权限列表")
    @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo findListByUserId(@PathVariable String id){
        List<SysRolePermission> userRoleList = rolePermissionService.findListByRid(id);
        return ResponseVo.ok(userRoleList);
    }

    @RequiresPermissions({"role:distribution"})
    @ApiOperation(value = "修改角色权限列表", notes = "修改角色权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "userRoleList", value = "用户角色列表", dataType = "List<SysUserRole>")
    })
    @PostMapping("/modify/{id}")
    public ResponseVo modifyUserRole(@PathVariable String id, @RequestBody List<SysRolePermission> rolePermissionList){
        return rolePermissionService.modifyUserRole(id, rolePermissionList) ? ResponseVo.ok("分配成功") : ResponseVo.fail("分配失败");
    }

}
