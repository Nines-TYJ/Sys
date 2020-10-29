package com.nines.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.nines.sys.entity.SysPermission;
import com.nines.sys.service.ISysPermissionService;
import com.nines.sys.vo.PageVo;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-21
 */
@RestController
@RequestMapping("/sys/permission")
@Api(description = "后台权限相关接口")
public class SysPermissionController {

    @Resource
    private ISysPermissionService permissionService;

    @ApiOperation(value = "获取权限信息", notes = "通过权限ID获取权限信息")
    @ApiImplicitParam(name = "id", value = "权限ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo getOneById(@PathVariable String id){
        SysPermission permission = permissionService.findOneById(id);
        return permission == null ? ResponseVo.fail("无效的ID") : ResponseVo.ok(permission);
    }

    @ApiOperation(value = "权限分页", notes = "权限列表分页")
    @ApiImplicitParam(name = "pageVo", value = "分页参数实体", dataType = "PageVo")
    @PostMapping("/data_page")
    public ResponseVo getDataPage(@RequestBody PageVo pageVo){
        return ResponseVo.ok(permissionService.findPage(pageVo));
    }

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

}
