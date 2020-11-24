package com.nines.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.nines.sys.entity.SysPermission;
import com.nines.sys.service.ISysPermissionService;
import com.nines.sys.util.PageUtil;
import com.nines.sys.vo.ResponseVo;
import com.nines.sys.vo.TreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-21
 */
@RequiresAuthentication
@RestController
@RequestMapping("/sys/permission")
@Api(tags = "后台权限相关接口")
public class SysPermissionController {

    @Resource
    private ISysPermissionService permissionService;

    @RequiresPermissions({"permission:select"})
    @ApiOperation(value = "获取权限信息", notes = "通过权限ID获取权限信息")
    @ApiImplicitParam(name = "id", value = "权限ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo getOneById(@PathVariable String id){
        SysPermission permission = permissionService.findOneById(id);
        return permission == null ? ResponseVo.fail("无效的ID") : ResponseVo.ok(permission);
    }

    @RequiresPermissions({"permission:select"})
    @ApiOperation(value = "权限分页", notes = "权限列表分页")
    @ApiImplicitParam(name = "pageVo", value = "分页参数实体", dataType = "PageVo")
    @PostMapping("/data_page")
    public ResponseVo getDataPage(@RequestBody PageUtil pageUtil){
        return ResponseVo.ok(permissionService.findPage(pageUtil));
    }

    @RequiresPermissions({"permission:insert", "permission:update"})
    @ApiOperation(value = "修改数据", notes = "根据ID修改数据")
    @PostMapping("/modif")
    public ResponseVo update(@RequestBody SysPermission permission){
        boolean result;
        if (StrUtil.hasBlank(permission.getId())){
            result = permissionService.addPermission(permission);
        }else {
            result = permissionService.updatePermission(permission);
        }
        return result ? ResponseVo.ok("操作成功") : ResponseVo.fail("操作失败");
    }

    @RequiresPermissions({"role:distribution"})
    @ApiOperation(value = "创建权限树", notes = "创建权限树")
    @GetMapping("/tree")
    public ResponseVo findTree(){
        List<TreeNode> tree = permissionService.createTree();
        return ResponseVo.ok(tree);
    }
}
