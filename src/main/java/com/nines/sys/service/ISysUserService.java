package com.nines.sys.service;

import com.nines.sys.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nines.sys.vo.DataPageVo;

import java.util.List;
import java.util.Map;

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
    int add(SysUser user);

    /**
     * 删除数据
     * @param id 用户ID
     * @return 操作条数
     */
    int delete(String id);

    /**
     * 修改用户
     * @param user 用户
     * @return 操作条数
     */
    int update(SysUser user);

    /**
     * 通过ID获取用户
     * @param id 用户ID
     * @return 用户实体
     */
    SysUser getOneById(String id);

    /**
     * 用户数据分页
     * @param dataPageVo 分页参数
     * @return 用户列表
     */
    Map<String, Object> getDataPage(DataPageVo dataPageVo);
}
