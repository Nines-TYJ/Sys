package com.nines.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nines.sys.entity.SysUserRole;
import com.nines.sys.exception.BizException;
import com.nines.sys.mapper.SysUserRoleMapper;
import com.nines.sys.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户与角色关系表 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public List<SysUserRole> findListByUserId(String userId) {
        return this.baseMapper.selectList(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean modifyUserRole(String userId, List<SysUserRole> userRoles) {
        List<SysUserRole> userRoleList = this.baseMapper.selectList(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId));
        if (userRoleList.size() > 0){
            // 删除用户原来的角色
            if (this.baseMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userId)) != userRoleList.size()){
                throw new BizException("删除条数与查找条数不符");
            }
        }
        // 添加新的角色数据
        if (userRoles.size() > 0){
            userRoles.forEach(userRole -> {
                userRole.setCreateTime(LocalDateTime.now());
                userRole.setUpdateTime(LocalDateTime.now());
            });
            return this.baseMapper.insetBatch(userRoles) > 0;
        }
        return false;
    }
}
