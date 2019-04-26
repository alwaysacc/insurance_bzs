package com.bzs.shiro;

import com.alibaba.fastjson.JSON;
import com.bzs.model.AccountInfo;
import com.bzs.model.MenuInfo;
import com.bzs.model.RoleInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.service.MenuInfoService;
import com.bzs.service.RoleInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import sun.rmi.runtime.Log;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private RoleInfoService roleInfoService;
    @Autowired
    private MenuInfoService menuInfoService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        AccountInfo accountInfo= (AccountInfo) SecurityUtils.getSubject().getPrincipal();
        String username=accountInfo.getLoginName();
        System.out.println("username"+username);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        //获取用户角色集
        List<RoleInfo> roleInfoList=roleInfoService.getUserRole(username);
        Set<String> roleSet=roleInfoList.stream().map(RoleInfo::getRoleName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);
        System.out.println(JSON.parse(String.valueOf(roleInfoList)));
        //获取用户权限集
        List<MenuInfo> menuInfoList=menuInfoService.getUserPermissions(username);
        Set<String> menuSet=menuInfoList.stream().map(MenuInfo::getMenuName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(menuSet);
        System.out.println(JSON.parse(String.valueOf(menuInfoList)));
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username= (String) authenticationToken.getPrincipal();
        String pass=   new String((char[]) authenticationToken.getCredentials());
        System.out.println(pass);
        AccountInfo accountInfo=accountInfoService.findByLoginName(username);
        System.out.println(accountInfo.getLoginPwd());
        if (accountInfo==null)
            throw new UnknownAccountException("用户名或密码错误");
        if (!accountInfo.getLoginPwd().equals(pass))
            throw new IncorrectCredentialsException("用户名或密码错误");
        if (AccountInfo.STATUS_LOCK.equals(accountInfo.getAccountState()))
            throw new LockedAccountException("账号已锁定");
        return new SimpleAuthenticationInfo(accountInfo,pass,getName());
    }
    /**
     * 清除权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
