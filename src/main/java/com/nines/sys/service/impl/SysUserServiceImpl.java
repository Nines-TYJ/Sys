package com.nines.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nines.sys.entity.SysUser;
import com.nines.sys.mapper.SysUserMapper;
import com.nines.sys.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public SysUser getUsernameAndStatusByUsername(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUser>().lambda()
                .select(SysUser::getPassWord, SysUser::getStatus)
                .eq(SysUser::getUserName, username));
    }

    @Override
    public List<String> getRoleByUsername(String username) {
        return this.baseMapper.getRoleByUsername(username);
    }

    @Override
    public List<String> getPermCodeByUsername(String username) {
        return this.baseMapper.getPermCodeByUsername(username);
    }
}
