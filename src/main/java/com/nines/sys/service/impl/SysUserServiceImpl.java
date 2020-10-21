package com.nines.sys.service.impl;

import com.nines.sys.entity.SysUser;
import com.nines.sys.mapper.SysUserMapper;
import com.nines.sys.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
