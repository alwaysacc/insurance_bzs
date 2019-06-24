package com.bzs.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "admin_menu")
public class AdminMenu {
    /**
     * 菜单/按钮ID
     */
    @Id
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 上级菜单ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 菜单/按钮名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 对应路由path
     */
    private String path;

    /**
     * 对应路由组件component
     */
    private String component;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型 0菜单 1按钮
     */
    private String type;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Double orderNum;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取菜单/按钮ID
     *
     * @return menu_id - 菜单/按钮ID
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置菜单/按钮ID
     *
     * @param menuId 菜单/按钮ID
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取上级菜单ID
     *
     * @return parent_id - 上级菜单ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置上级菜单ID
     *
     * @param parentId 上级菜单ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取菜单/按钮名称
     *
     * @return menu_name - 菜单/按钮名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置菜单/按钮名称
     *
     * @param menuName 菜单/按钮名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * 获取对应路由path
     *
     * @return path - 对应路由path
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置对应路由path
     *
     * @param path 对应路由path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取对应路由组件component
     *
     * @return component - 对应路由组件component
     */
    public String getComponent() {
        return component;
    }

    /**
     * 设置对应路由组件component
     *
     * @param component 对应路由组件component
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 获取权限标识
     *
     * @return perms - 权限标识
     */
    public String getPerms() {
        return perms;
    }

    /**
     * 设置权限标识
     *
     * @param perms 权限标识
     */
    public void setPerms(String perms) {
        this.perms = perms;
    }

    /**
     * 获取图标
     *
     * @return icon - 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取类型 0菜单 1按钮
     *
     * @return type - 类型 0菜单 1按钮
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型 0菜单 1按钮
     *
     * @param type 类型 0菜单 1按钮
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取排序
     *
     * @return order_num - 排序
     */
    public Double getOrderNum() {
        return orderNum;
    }

    /**
     * 设置排序
     *
     * @param orderNum 排序
     */
    public void setOrderNum(Double orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}