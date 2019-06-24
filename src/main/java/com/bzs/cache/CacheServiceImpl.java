package com.bzs.cache;

import com.bzs.model.AccountInfo;
import com.bzs.model.FebsConstant;
import com.bzs.model.Role;
import com.bzs.model.TMenu;
import com.bzs.redis.RedisService;
import com.bzs.service.AccountInfoService;
import com.bzs.service.RoleService;
import com.bzs.service.TMenuService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.startup.UserConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-18 14:28
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService{

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TMenuService menuService;

    @Autowired
    private AccountInfoService userService;

    /*@Autowired
    private UserConfigService userConfigService;*/

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void testConnect() throws Exception {
        this.redisService.exists("test");
    }

    @Override
    public AccountInfo getAccountInfo(String username) throws Exception {
        String userString = this.redisService.get(FebsConstant.USER_CACHE_PREFIX + username);
        if (StringUtils.isBlank(userString))
            throw new Exception();
        else
            return this.mapper.readValue(userString, AccountInfo.class);
    }

    @Override
    public List<Role> getRoles(String username) throws Exception {
        String roleListString = this.redisService.get(FebsConstant.USER_ROLE_CACHE_PREFIX + username);
        if (StringUtils.isBlank(roleListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, Role.class);
            return this.mapper.readValue(roleListString, type);
        }
    }

    @Override
    public List<TMenu> getPermissions(String username) throws Exception {
        String permissionListString = this.redisService.get(FebsConstant.USER_PERMISSION_CACHE_PREFIX + username);
        if (StringUtils.isBlank(permissionListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, TMenu.class);
            return this.mapper.readValue(permissionListString, type);
        }
    }

/*    @Override
    public UserConfig getUserConfig(String userId) throws Exception {
        String userConfigString = this.redisService.get(FebsConstant.USER_CONFIG_CACHE_PREFIX + userId);
        if (StringUtils.isBlank(userConfigString))
            throw new Exception();
        else
            return this.mapper.readValue(userConfigString, UserConfig.class);
    }*/

    @Override
    public void saveAccountInfo(AccountInfo user) throws Exception {
        String accountInfoId = user.getAccountId();
        this.deleteAccountInfo(accountInfoId);
        redisService.set(FebsConstant.USER_CACHE_PREFIX + accountInfoId, mapper.writeValueAsString(user));
    }

    @Override
    public void saveAccountInfo(String accountId) throws Exception {
        AccountInfo user = userService.findBy("accountId",accountId);
        this.deleteAccountInfo(accountId);
        redisService.set(FebsConstant.USER_CACHE_PREFIX + accountId, mapper.writeValueAsString(user));
    }

    @Override
    public void saveRoles(String accountId) throws Exception {
        List<Role> roleList = this.roleService.findUserRoleByAccountId(accountId);
        if (!roleList.isEmpty()) {
            this.deleteRoles(accountId);
            redisService.set(FebsConstant.USER_ROLE_CACHE_PREFIX + accountId, mapper.writeValueAsString(roleList));
        }
    }

    @Override
    public void savePermissions(String username) throws Exception {
        List<TMenu> permissionList = this.menuService.findUserPermissions(username);
        if (!permissionList.isEmpty()) {
            this.deletePermissions(username);
            redisService.set(FebsConstant.USER_PERMISSION_CACHE_PREFIX + username, mapper.writeValueAsString(permissionList));
        }
    }

   /* @Override
    public void saveUserConfigs(String userId) throws Exception {
        UserConfig userConfig = this.userConfigService.findByUserId(userId);
        if (userConfig != null) {
            this.deleteUserConfigs(userId);
            redisService.set(FebsConstant.USER_CONFIG_CACHE_PREFIX + userId, mapper.writeValueAsString(userConfig));
        }
    }*/

    @Override
    public void deleteAccountInfo(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(FebsConstant.USER_CACHE_PREFIX + username);
    }

    @Override
    public void deleteRoles(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(FebsConstant.USER_ROLE_CACHE_PREFIX + username);
    }

    @Override
    public void deletePermissions(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(FebsConstant.USER_PERMISSION_CACHE_PREFIX + username);
    }

    /*@Override
    public void deleteUserConfigs(String userId) throws Exception {
        redisService.del(FebsConstant.USER_CONFIG_CACHE_PREFIX + userId);
    }*/
}
