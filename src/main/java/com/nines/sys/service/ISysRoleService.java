package com.nines.sys.service;

import com.nines.sys.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nines.sys.vo.PageVo;

import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 添加角色
     * @param role 角色
     * @return 成功与否
     */
    boolean addRole(SysRole role);

    /**
     * 更新角色
     * @param role 角色
     * @return 成功与否
     */
    boolean updateRole(SysRole role);

    /**
     * 删除角色(同时删除用户角色表和角色权限表关联数据)
     * @param id 角色ID
     * @return 成功与否
     */
    boolean deleteRoleById(String id);

    /**
     * 通过ID查找一个角色
     * @param id 角色id
     * @return 角色实体
     */
    SysRole findOneById(String id);

    /**
     * 获取角色列表
     * @param pageVo 分页参数
     * @return map
     */
    Map<String, Object> findPage(PageVo pageVo);

}
