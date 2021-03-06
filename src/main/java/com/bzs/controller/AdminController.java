package com.bzs.controller;
import com.bzs.dao.AdminMapper;
import com.bzs.redis.RedisUtil;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.Admins;
import com.bzs.service.AdminService;
import com.bzs.utils.redisConstant.RedisConstant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
* Created by dl on 2019/06/17.
*/
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public Result login(String username,String password,String code,boolean rememberMe) {
        if (StringUtils.isBlank(code)){
            return ResultGenerator.genFailResult("验证码不能为空");
        }
//        Session session= SecurityUtils.getSubject().getSession();

//        String sessionCode= (String) session.getAttribute(CODE_KEY);
//        if (!code.equalsIgnoreCase(sessionCode)){
//            return ResultGenerator.genFailResult("验证码错误");
//        }
        Admins admin=adminService.findBy("loginName",username);
        password= MD5Utils.encrypt(username.toLowerCase(),password);
        log.info(password);
        UsernamePasswordToken token=new UsernamePasswordToken(username,password,rememberMe);
        if (admin==null)
        return ResultGenerator.genFailResult("用户名或密码错误");
        else if (!admin.getLoginPwd().equals(password))
            return ResultGenerator.genFailResult("用户名或密码错误");
        else if ("1".equals(admin.getStatus()))
            return ResultGenerator.genFailResult("账号已锁定");
        //修改登录时间
        adminService.updateLoginTime(null,username);
        Map map=new HashMap();
        map.put("user",admin);
        map.put("token",token);
        return ResultGenerator.genSuccessResult(map);
      /*  try {
            Subject subject=SecurityUtils.getSubject();
            if (subject!=null)
                subject.logout();
            System.out.println(subject.getPrincipals());
            subject.login(token);
            //修改登录时间
            adminService.updateLoginTime(null,username);
            //session.setAttribute("userName",username);
            //session.setAttribute("accountInfo",accountInfo);
            Map map=new HashMap();
            map.put("user",admin);
            map.put("token",token);
            return ResultGenerator.genSuccessResult(map);
        }catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e){
            return ResultGenerator.genFailResult(e.getMessage());
        }catch (AuthenticationException e){
            return  ResultGenerator.genFailResult("认证失败"+e.getMessage());
        }*/
    }
    @PostMapping("/updateAdmin")
    public Result updateAdmin(Admins admin) {
        return ResultGenerator.genSuccessResult(adminService.updateAdmin(admin));
    }
    @PostMapping("/checkAdminLoginName")
    public Result checkAdminLoginName(String loginName) {
        return  ResultGenerator.genSuccessResult(adminService.checkAdminLoginName().contains(loginName));
    }
    @PostMapping("/add")
    public Result add(Admins admin) {
        admin.setLoginPwd(MD5Utils.encrypt(admin.getLoginName().toLowerCase(),admin.getLoginPwd()));
        adminService.save(admin);
        HashSet set;
        if (!redisUtil.hasKey(RedisConstant.ADMIN_LOGIN_NAME_LIST)){
            set=adminMapper.getAdminLoginName();
            redisUtil.set(RedisConstant.ADMIN_LOGIN_NAME_LIST,set,720000);
        }else{
            log.info("账号存入redis");
            set= (HashSet) redisUtil.get(RedisConstant.ADMIN_LOGIN_NAME_LIST);
            set.add(admin.getLoginName());
            redisUtil.set(RedisConstant.ADMIN_LOGIN_NAME_LIST,set,720000);
        }
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        adminService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Admins admin) {
        adminService.update(admin);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Admins admin = adminService.findById(id);
        return ResultGenerator.genSuccessResult(admin);
    }
    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size,String adminName) {
        PageHelper.startPage(page, size);
        List list = adminService.getAdminList(adminName);
        PageInfo pageInfo = new PageInfo(list);
        System.out.println(redisUtil.get("bzs*"));
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
