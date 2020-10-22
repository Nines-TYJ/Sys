package com.nines.sys.mapper;

import com.nines.sys.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author Nines
 * @since 2020-10-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("select name from sys_role where id in (" +
            "select role_id from sys_user_role where user_id = (" +
            "select id from sys_user where user_name = #{username}))")
    List<String> getRoleByUsername(@Param("username") String username);

    @Select("select perm_code from sys_permission where id in (" +
            "select permission_id from sys_role_permission where role_id in (" +
            "select role_id from sys_user_role where user_id = (" +
            "select id from sys_user where user_name = #{username})))")
    List<String> getPermCodeByUsername(@Param("username") String username);
}
