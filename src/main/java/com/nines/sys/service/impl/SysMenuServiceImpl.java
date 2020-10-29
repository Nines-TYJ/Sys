package com.nines.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nines.sys.entity.SysMenu;
import com.nines.sys.entity.SysPermission;
import com.nines.sys.enums.StatusEnum;
import com.nines.sys.mapper.SysMenuMapper;
import com.nines.sys.mapper.SysPermissionMapper;
import com.nines.sys.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nines.sys.util.MyEnumUtil;
import com.nines.sys.vo.MenuTreeNodeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-21
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Resource
    private SysPermissionMapper permissionMapper;

    @Override
    public boolean addMeun(SysMenu menu) {
        // 菜单名不能为空
        if (StrUtil.hasBlank(menu.getName())){
            return false;
        }
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        return this.baseMapper.insert(menu) > 0;
    }

    @Override
    public boolean updateMeun(SysMenu menu) {
        SysMenu updateMeun = this.baseMapper.selectById(menu.getId());
        if (!StrUtil.hasBlank(menu.getParentId()) && !updateMeun.getParentId().equals(menu.getParentId())){
            updateMeun.setParentId(menu.getParentId());
        }
        if (!StrUtil.hasBlank(menu.getName()) && !updateMeun.getName().equals(menu.getName())){
            updateMeun.setName(menu.getName());
        }
        if (!StrUtil.hasBlank(menu.getRemark()) && !updateMeun.getRemark().equals(menu.getRemark())){
            updateMeun.setRemark(menu.getRemark());
        }
        if (!StrUtil.hasBlank(menu.getUrl()) && !updateMeun.getUrl().equals(menu.getUrl())){
            updateMeun.setUrl(menu.getUrl());
        }
        if (!updateMeun.getSort().equals(menu.getSort())){
            updateMeun.setSort(menu.getSort());
        }
        if (MyEnumUtil.isInclude(StatusEnum.class, menu.getStatus()) && !updateMeun.getStatus().equals(menu.getStatus())){
            updateMeun.setStatus(menu.getStatus());
        }
        updateMeun.setUpdateTime(LocalDateTime.now());
        return this.baseMapper.updateById(updateMeun) > 0;
    }

    @Override
    public boolean deleteMeunById(String id) {
        SysMenu meun = this.baseMapper.selectById(id);
        // 菜单不存在
        if (meun == null){
            return false;
        }
        // 判断该菜单下是否还有子菜单
        if (this.baseMapper.selectList(new QueryWrapper<SysMenu>().lambda().eq(SysMenu::getParentId, id)).size() > 0){
            return false;
        }
        // 判断该菜单下是否还有权限
        if (permissionMapper.selectList(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getMenuId, id)).size() > 0){
            return false;
        }
        // 删除菜单
        return this.baseMapper.deleteById(id) > 0;
    }

    @Override
    public SysMenu findOneById(String id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<MenuTreeNodeVo> createTree() {
        // 找到顶级菜单或权限
        List<SysMenu> menuList = findMenuList("0");
        return addNodeToTree(menuList);
    }


    private List<MenuTreeNodeVo> addNodeToTree(List<SysMenu> menuList){
        // 创建树
        List<MenuTreeNodeVo> tree = new ArrayList<>();
        // 子节点
        List childrenList = new ArrayList();
        // 遍历菜单
        menuList.forEach(menu -> {
            // 查询菜单是否有子菜单
            List<SysMenu> childrenMenuList = findMenuList(menu.getId());
            if (childrenMenuList.size() > 0){
                // 递归获取子树
                List<MenuTreeNodeVo> childrenTree = addNodeToTree(childrenMenuList);
                childrenList.addAll(childrenTree);
            }
            // 查询菜单下是否有权限
            List<SysPermission> childrenPermissionList = findPermissionList(menu.getId());
            if (childrenPermissionList.size() > 0){
                childrenList.addAll(childrenPermissionList);
            }
            tree.add(new MenuTreeNodeVo(menu.getName(), childrenList));
        });
        return tree;
    }

    /**
     * 通过父级菜单id获取菜单列表
     * @param parentId 父级ID
     * @return 菜单列表
     */
    private List<SysMenu> findMenuList(String parentId){
        return this.baseMapper.selectList(new QueryWrapper<SysMenu>().lambda()
                .eq(SysMenu::getParentId, parentId)
                // 只获取状态正常的
                .eq(SysMenu::getStatus, StatusEnum.NORMAL.getValue())
                // 升序排列
                .orderByAsc(SysMenu::getSort));
    }

    /**
     * 通过菜单id获取权限列表
     * @param menuId 菜单id
     * @return 权限列表
     */
    private List<SysPermission> findPermissionList(String menuId){
        return permissionMapper.selectList(new QueryWrapper<SysPermission>().lambda()
                .eq(SysPermission::getMenuId, menuId)
                // 只获取状态正常的
                .eq(SysPermission::getStatus, StatusEnum.NORMAL.getValue())
                // 升序排列
                .orderByAsc(SysPermission::getSort));
    }

}
