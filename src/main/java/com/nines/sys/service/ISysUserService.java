package com.nines.sys.service;

import com.nines.sys.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author Nines
 * @since 2020-10-22
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 通过username 获取 用户信息
     * @param username 用户名
     * @return 用户密码和状态
     */
    SysUser getUserByUsername(String username);

    /**
     * 通过username 获取 用户角色列表
     * @param username 用户名
     * @return 角色列表
     */
    List<String> getRoleByUsername(String username);

    /**
     * 通过username 获取 用户权限列表
     * @param username 用户名
     * @return 权限列表
     */
    List<String> getPermCodeByUsername(String username);

    /**
     * 新增用户
     * @param user 用户
     * @return 操作条数
     */
    int addUser(SysUser user);

}
