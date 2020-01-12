package com.bzs.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bzs.dao.AccountInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by alwaysacc on 2019/04/11.
*/
@RestController
@RequestMapping("/login")
public class LoginController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String CODE_KEY = "_code";

    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private AccountInfoMapper accountInfoMapper;

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username,String password,String code,boolean rememberMe,String openId) {
        System.out.println(username);
        System.out.println(password);
        if (StringUtils.isBlank(code)){
            return ResultGenerator.genFailResult("验证码不能为空");
        }
//        Session session=SecurityUtils.getSubject().getSession();

//        String sessionCode= (String) session.getAttribute(CODE_KEY);
//        if (!code.equalsIgnoreCase(sessionCode)){
//            return ResultGenerator.genFailResult("验证码错误");
//        }

        password=MD5Utils.encrypt(username.toLowerCase(),password);
        UsernamePasswordToken token=new UsernamePasswordToken(username,password,rememberMe);
        AccountInfo accountInfo=accountInfoService.findByLoginName(username);
        if (accountInfo==null)
            return ResultGenerator.genFailResult("用户名或密码错误");
        else if (!accountInfo.getLoginPwd().equals(password))
            return ResultGenerator.genFailResult("用户名或密码错误");
        else if (1==accountInfo.getAccountState())
            return ResultGenerator.genFailResult("账号已锁定");
        else if (2==accountInfo.getAccountState())
            return ResultGenerator.genFailResult("账号待审核");

        if (StrUtil.isNotBlank(openId)){
            accountInfo.setOpenId(openId);
            accountInfoMapper.updateByPrimaryKeySelective(accountInfo);
        }
        accountInfoService.updateLoginTime(username);
        return ResultGenerator.genSuccessResult(accountInfo);
    }
    private Map<String,Object> getUserInfo(String username){
        Map<String,Object> userInfo=null;
        return userInfo;
    }


}
