package com.bzs.service;
import com.bzs.model.TMenu;
import com.bzs.utils.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/11.
 */
public interface TMenuService extends Service<TMenu> {
    List<TMenu> findUserPermissions(@Param("accountId") String accountId);

    List<TMenu> findUserMenus(@Param("accountId") String accountId);

    Map<String, Object> findMenus(TMenu menu);

    List<TMenu> findMenuList(TMenu menu);

    void createMenu(TMenu menu);

    void updateMenu(TMenu menu) throws Exception;

    /**
     * 递归删除菜单/按钮
     *
     * @param menuIds menuIds
     */
    void deleteMeuns(String[] menuIds) throws Exception;

}
