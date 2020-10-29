package com.nines.sys.controller;


import cn.hutool.core.util.StrUtil;
import com.nines.sys.entity.SysRole;
import com.nines.sys.service.ISysRoleService;
import com.nines.sys.vo.PageVo;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@RestController
@RequestMapping("/sys/role")
@Api(description = "后台角色相关接口")
public class SysRoleController {

    @Resource
    private ISysRoleService roleService;

    @ApiOperation(value = "获取角色信息", notes = "通过角色ID获取角色信息")
    @ApiImplicitParam(name = "id", value = "角色ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo getOneById(@PathVariable String id){
        SysRole role = roleService.findOneById(id);
        return role == null ? ResponseVo.fail("无效的ID") : ResponseVo.ok(role);
    }

    @ApiOperation(value = "角色分页", notes = "角色列表分页")
    @ApiImplicitParam(name = "pageVo", value = "分页参数实体", dataType = "PageVo")
    @PostMapping("/data_page")
    public ResponseVo getDataPage(@RequestBody PageVo pageVo){
        return ResponseVo.ok(roleService.findPage(pageVo));
    }

    @ApiOperation(value = "新增或修改数据", notes = "根据ID判断新增或修改数据")
    @PostMapping("/modify")
    public ResponseVo addOrUpdate(@RequestBody SysRole role){
        boolean result;
        if (StrUtil.hasBlank(role.getId())){
            result = roleService.addRole(role);
        }else {
            result = roleService.updateRole(role);
        }
        return result ? ResponseVo.ok("操作成功") : ResponseVo.fail("操作失败");
    }

    @ApiOperation(value = "删除角色", notes = "根据ID删除数据")
    @ApiImplicitParam(name = "id", value = "角色ID", dataType = "String")
    @PostMapping("/delete/{id}")
    public ResponseVo delete(@PathVariable String id) {
        return roleService.deleteRoleById(id) ? ResponseVo.ok("操作成功") : ResponseVo.fail("操作失败");
    }
}
