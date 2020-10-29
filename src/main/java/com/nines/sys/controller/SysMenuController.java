package com.nines.sys.controller;


import com.nines.sys.entity.SysMenu;
import com.nines.sys.service.ISysMenuService;
import com.nines.sys.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Nines
 * @since 2020-10-21
 */
@RestController
@RequestMapping("/sys/menu")
@Api(description = "后台菜单相关接口")
public class SysMenuController {

    @Resource
    private ISysMenuService menuService;

    @ApiOperation(value = "获取菜单信息", notes = "获取单个菜单详情")
    @ApiImplicitParam(name = "id", value = "菜单ID", dataType = "String")
    @GetMapping("/{id}")
    public ResponseVo findOneById(@PathVariable String id){
        SysMenu menu = menuService.findOneById(id);
        return menu == null ? ResponseVo.fail("无效id") : ResponseVo.ok(menu);
    }

    @ApiOperation(value = "添加或修改数据", notes = "根据id判断应添加还是修改")
    @ApiImplicitParam(name = "menu", value = "菜单实体", dataType = "SysMenu")
    @PostMapping("/modif")
    public ResponseVo addOrUpdate(@RequestBody SysMenu menu){
        boolean result;
        if (menu.getId() == null){
            result = menuService.addMeun(menu);
        }else {
            result = menuService.updateMeun(menu);
        }
        return result ? ResponseVo.ok("操作成功") : ResponseVo.fail("操作失败");
    }

    @ApiOperation(value = "菜单权限树", notes = "获取菜单权限树")
    @GetMapping("/tree")
    public ResponseVo findMenuTree(){
        return ResponseVo.ok(menuService.createTree());
    }

}
