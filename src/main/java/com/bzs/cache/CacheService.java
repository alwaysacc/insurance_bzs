package com.bzs.cache;

import com.bzs.model.AccountInfo;
import com.bzs.model.Role;
import com.bzs.model.TMenu;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-18 14:15
 */
public interface CacheService {
    /**
     * 测试 Redis是否连接成功
     */
    void testConnect() throws Exception;

    /**
     * 从缓存中获取用户
     *
     * @param accountId 用户id
     * @return User
     */
    AccountInfo getAccountInfo(String accountId) throws Exception;

    /**
     * 从缓存中获取用户角色
     *
     * @param accountId 用户id
     * @return 角色集
     */
    List<Role> getRoles(String accountId) throws Exception;

    /**
     * 从缓存中获取用户权限
     *
     * @param accountId 用户id
     * @eturn 权限集
     */
    List<TMenu> getPermissions(String accountId) throws Exception;

    /**
     * 从缓存中获取用户个性化配置
     *
     * @param userId 用户 ID
     * @return 个性化配置
     */
//    UserConfig getUserConfig(String userId) throws Exception;

    /**
     * 缓存用户信息，只有当用户信息是查询出来的，完整的，才应该调用这个方法
     * 否则需要调用下面这个重载方法
     *
     * @param user 用户信息
     */
    void saveAccountInfo(AccountInfo user) throws Exception;

    /**
     * 缓存用户信息
     *
     * @param accountId 用户id
     */
    void saveAccountInfo(String accountId) throws Exception;

    /**
     * 缓存用户角色信息
     *
     * @param accountId 用户id
     */
    void saveRoles(String accountId) throws Exception;

    /**
     * 缓存用户权限信息
     *
     * @param accountId 用户id
     */
    void savePermissions(String accountId) throws Exception;

    /**
     * 缓存用户个性化配置
     *
     * @param userId 用户 ID
     */
//    void saveUserConfigs(String userId) throws Exception;

    /**
     * 删除用户信息
     *
     * @param accountId 用户id
     */
    void deleteAccountInfo(String accountId) throws Exception;

    /**
     * 删除用户角色信息
     *
     * @param accountId 用户名
     */
    void deleteRoles(String accountId) throws Exception;

    /**
     * 删除用户权限信息
     *
     * @param accountId 用户名
     */
    void deletePermissions(String accountId) throws Exception;

    /**
     * 删除用户个性化配置
     *
     * @param userId 用户 ID
     */
//    void deleteUserConfigs(String userId) throws Exception;
}
