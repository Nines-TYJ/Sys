package com.nines.sys.service;

import com.nines.sys.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Nines
 * @since 2020-10-21
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 添加菜单
     * @param menu 菜单
     * @return 成功与否
     */
    boolean addMeun(SysMenu menu);

    /**
     * 更新菜单
     * @param menu 菜单
     * @return 成功与否
     */
    boolean updateMeun(SysMenu menu);

    /**
     * 删除菜单（同时将菜单下的子菜单与功能给上级菜单）
     * @param id 菜单ID
     * @return 成功与否
     */
    boolean deleteMeunById(String id);

    /**
     * 通过ID查找一个菜单
     * @param id 菜单id
     * @return 菜单实体
     */
    SysMenu findOneById(String id);

}
