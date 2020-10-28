package com.nines.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nines.sys.entity.SysRolePermission;
import com.nines.sys.mapper.SysRolePermissionMapper;
import com.nines.sys.service.ISysRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色与权限关系表 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Override
    public List<SysRolePermission> findListByRid(String roleId) {
        return this.baseMapper.selectList(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean modifyUserRole(String roleId, List<SysRolePermission> rolePermissions) {
        // 删除原来角色的权限
        if (this.baseMapper.deleteById(roleId) > 0){
            // 添加新的角色的权限
            if (rolePermissions.size() > 0){
                rolePermissions.forEach(rolePermission -> {
                    rolePermission.setCreateTime(LocalDateTime.now());
                    rolePermission.setUpdateTime(LocalDateTime.now());
                });
                return this.baseMapper.insetBatch(rolePermissions) > 0;
            }
        }
        return false;
    }
}
