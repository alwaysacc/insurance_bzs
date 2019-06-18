package com.bzs.controller;
import com.bzs.dao.AdminMapper;
import com.bzs.model.AccountInfo;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Admin;
import com.bzs.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by dl on 2019/06/17.
*/
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private AdminMapper adminMapper;


    @PostMapping("/login")
    public Result login(String username,String password,String code,boolean rememberMe) {
        if (StringUtils.isBlank(code)){
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        Session session= SecurityUtils.getSubject().getSession();

//        String sessionCode= (String) session.getAttribute(CODE_KEY);
//        if (!code.equalsIgnoreCase(sessionCode)){
//            return ResultGenerator.genFailResult("验证码错误");
//        }
        Admin admin=adminService.findBy("loginName",username);
        password= MD5Utils.encrypt(username.toLowerCase(),password);
        UsernamePasswordToken token=new UsernamePasswordToken(username,password,rememberMe);
        if (admin==null)
            return ResultGenerator.genFailResult("用户名或密码错误");
        else if (!admin.getLoginPwd().equals(password))
            return ResultGenerator.genFailResult("用户名或密码错误");
        else if ("1".equals(admin.getStatus()))
            return ResultGenerator.genFailResult("账号已锁定");
        try {
            Subject subject=SecurityUtils.getSubject();
            if (subject!=null)
                subject.logout();
            System.out.println(subject.getPrincipals());
            subject.login(token);
            //修改登录时间
            adminService.updateLoginTime(null,username);
            //session.setAttribute("userName",username);
            //session.setAttribute("accountInfo",accountInfo);
            return ResultGenerator.genSuccessResult(admin);
        }catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e){
            return ResultGenerator.genFailResult(e.getMessage());
        }catch (AuthenticationException e){
            return  ResultGenerator.genFailResult("认证失败"+e.getMessage());
        }
    }
    @PostMapping("/add")
    public Result add(Admin admin) {
        adminService.save(admin);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        adminService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Admin admin) {
        adminService.update(admin);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Admin admin = adminService.findById(id);
        return ResultGenerator.genSuccessResult(admin);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Admin> list = adminService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
