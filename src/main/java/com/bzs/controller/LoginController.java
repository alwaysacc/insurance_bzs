package com.bzs.controller;

import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by alwaysacc on 2019/04/11.
*/
@RestController
@RequestMapping("/account/info")
public class LoginController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String CODE_KEY = "_code";

    @Resource
    private AccountInfoService accountInfoService;
    
    @PostMapping("/login")
    public Result add(String username,String password,String code,boolean rememberMe) {
        if (StringUtils.isBlank(code)){
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        Session session=SecurityUtils.getSubject().getSession();
        String sessionCode= (String) session.getAttribute(CODE_KEY);
//        if (!code.equalsIgnoreCase(sessionCode)){
//            return ResultGenerator.genFailResult("验证码错误");
//        }
        password=MD5Utils.encrypt(username.toLowerCase(),password);
        UsernamePasswordToken token=new UsernamePasswordToken(username,password,rememberMe);
        Subject subject=SecurityUtils.getSubject();
        try {
            if (subject!=null)
                subject.logout();
            subject.login(token);
            //修改登录时间
            accountInfoService.updateLoginTime(username);
            return ResultGenerator.genSuccessResult();
        }catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e){
            return ResultGenerator.genFailResult(e.getMessage());
        }catch (AuthenticationException e){
            return  ResultGenerator.genFailResult("认证失败");
        }
    }

}
