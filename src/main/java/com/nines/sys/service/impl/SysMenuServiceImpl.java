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
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteMeunById(String id) {
        SysMenu meun = this.baseMapper.selectById(id);
        // 菜单不存在
        if (meun == null){
            return false;
        }
        if (this.baseMapper.deleteById(id) > 0){
            // 修改子级菜单的父级id为当前菜单的父级ID
            List<SysMenu> childMeunList = this.baseMapper.selectList(new QueryWrapper<SysMenu>().lambda().eq(SysMenu::getParentId, id));
            if (childMeunList.size() > 0){
                childMeunList.forEach(childMeun -> childMeun.setParentId(meun.getParentId()));
            }
            // 修改权限的菜单id为当前菜单的父级菜单ID
            List<SysPermission> childPermissionList = permissionMapper.selectList(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getMenuId, id));
            if (childPermissionList.size() > 0){
                childPermissionList.forEach(childPermission -> childPermission.setMenuId(meun.getParentId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public SysMenu findOneById(String id) {
        return this.baseMapper.selectById(id);
    }
}
