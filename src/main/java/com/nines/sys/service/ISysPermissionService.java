package com.nines.sys.service;

import com.nines.sys.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nines.sys.util.PageUtil;
import com.nines.sys.vo.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author Nines
 * @since 2020-10-21
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 添加权限
     * @param permission 权限
     * @return 成功与否
     */
    boolean addPermission(SysPermission permission);

    /**
     * 更新权限
     * @param permission 权限
     * @return 成功与否
     */
    boolean updatePermission(SysPermission permission);

    /**
     * 删除权限（同时删除角色权限表相关数据）
     * @param id 权限ID
     * @return 成功与否
     */
    boolean deletePermissionById(String id);

    /**
     * 通过ID查找一个权限
     * @param id 权限id
     * @return 权限实体
     */
    SysPermission findOneById(String id);

    /**
     * 获取权限列表
     * @param pageUtil 分页参数
     * @return map
     */
    PageUtil findPage(PageUtil pageUtil);

    /**
     * 生产菜单权限树
     * @return 树
     */
    List<TreeNode> createTree();

}
