package com.bzs.dao;

import com.bzs.model.TMenu;
import com.bzs.utils.Mapper;

import java.util.List;

public interface TMenuMapper extends Mapper<TMenu> {

    List<TMenu> findUserPermissions(String userName);

    List<TMenu> findUserMenus(String userName);

    /**
     * 查找当前菜单/按钮关联的用户 ID
     *
     * @param menuId menuId
     * @return 用户 ID集合
     */
    List<String> findUserIdsByMenuId(String menuId);

    /**
     * 递归删除菜单/按钮
     *
     * @param menuId menuId
     */
    void deleteMenus(String menuId);
}