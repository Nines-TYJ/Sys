package com.nines.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nines.sys.entity.SysRole;
import com.nines.sys.entity.SysRolePermission;
import com.nines.sys.entity.SysUserRole;
import com.nines.sys.enums.StatusEnum;
import com.nines.sys.mapper.SysRoleMapper;
import com.nines.sys.mapper.SysRolePermissionMapper;
import com.nines.sys.mapper.SysUserRoleMapper;
import com.nines.sys.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nines.sys.util.MyEnumUtil;
import com.nines.sys.vo.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysRolePermissionMapper rolePermissionMapper;

    @Override
    public boolean addRole(SysRole role) {
        return this.baseMapper.insert(role) > 0;
    }

    @Override
    public boolean updateRole(SysRole role) {
        // 不为空且不相等才更新
        SysRole updateRole = this.baseMapper.selectById(role.getId());
        if (!StrUtil.hasBlank(role.getRemark()) && !updateRole.getRemark().equals(role.getRemark())){
            updateRole.setRemark(role.getRemark());
        }
        if (MyEnumUtil.isInclude(StatusEnum.class, role.getStatus()) && !updateRole.getStatus().equals(role.getStatus())){
            updateRole.setStatus(role.getStatus());
        }
        updateRole.setUpdateTime(LocalDateTime.now());
        return this.baseMapper.updateById(updateRole) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRoleById(String id) {
        SysRole role = this.baseMapper.selectById(id);
        // 角色不存在
        if (role == null){
            return false;
        }
        // 删除角色成功
        if (this.baseMapper.deleteById(id) > 0){
            // 删除用户角色表中相关的用户
            userRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getRoleId, id));
            // 删除角色权限表中相关的权限
            rolePermissionMapper.delete(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, id));
            return true;
        }
        return false;
    }

    @Override
    public SysRole findOneById(String id) {
        return this.baseMapper.selectById(id);
    }


    @Override
    public Map<String, Object> findPage(PageVo pageVo) {
        Page<SysRole> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.hasBlank(pageVo.getName())){
            queryWrapper.like("name", pageVo.getName());
        }
        IPage<SysRole> iPage = this.baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> dataPage = new HashMap<>();
        dataPage.put("datas", iPage.getRecords());
        dataPage.put("totals", iPage.getTotal());
        return dataPage;
    }
}
