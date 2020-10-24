package com.nines.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nines.sys.entity.SysUser;
import com.nines.sys.mapper.SysUserMapper;
import com.nines.sys.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nines.sys.util.PasswordMd5Util;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author Nines
 * @since 2020-10-22
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public SysUser getUserByUsername(String username) {
        return this.baseMapper.selectOne(new QueryWrapper<SysUser>().lambda()
//                .select(SysUser::getPassWord, SysUser::getStatus)
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

    @Override
    public int addUser(SysUser user) {
        // 昵称为空时直接设置为用户名
        if (StrUtil.hasBlank(user.getNickName())){
            user.setNickName(user.getUserName());
        }
        // 设置密码和盐
        Map<String, String> map = PasswordMd5Util.encryption(user.getPassWord());
        user.setPassWord(map.get("encryptPassword"));
        user.setSalt(map.get("salt"));
        // 设置数据创建时间
        LocalDateTime dataTime = LocalDateTime.now();
        user.setCreateTime(dataTime);
        user.setUpdateTime(dataTime);
        return 0;
    }

}
