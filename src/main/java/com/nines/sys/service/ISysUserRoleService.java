package com.nines.sys.service;

import com.nines.sys.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户与角色关系表 服务类
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 通过userId查找List
     * @param userId 用户ID
     * @return List<SysUserRole>
     */
    List<SysUserRole> findListByUserId(String userId);

    /**
     * 修改用户拥有的角色
     * @param  userId 用户ID
     * @param userRoles 用户角色列表
     * @return 成功与否
     */
    boolean modifyUserRole(String userId, List<SysUserRole> userRoles);

}
