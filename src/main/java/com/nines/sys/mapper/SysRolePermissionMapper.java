package com.nines.sys.mapper;

import com.nines.sys.entity.SysRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色与权限关系表 Mapper 接口
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 批量添加新权限
     * @param rolePermissionList 角色列表
     * @return 成功条数
     */
    int insetBatch(List<SysRolePermission> rolePermissionList);

}
