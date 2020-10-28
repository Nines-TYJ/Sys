package com.nines.sys.service;

import com.nines.sys.entity.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色与权限关系表 服务类
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 通过roleId查找List
     * @param roleId 角色id
     * @return List<RolePermission>
     */
    List<SysRolePermission> findListByRid(String roleId);

    /**
     * 修改用户角色
     * @param roleId 角色id
     * @param rolePermissions 角色权限列表
     * @return 成功与否
     */
    boolean modifyUserRole(String roleId, List<SysRolePermission> rolePermissions);

}
