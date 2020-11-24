package com.nines.sys.mapper;

import com.nines.sys.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户与角色关系表 Mapper 接口
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 批量添加新角色
     * @param userRoleList 角色列表
     * @return 成功条数
     */
    int insertBatch(List<SysUserRole> userRoleList);

}
