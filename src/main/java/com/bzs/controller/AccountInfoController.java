package com.bzs.controller;
import com.bzs.shiro.FebsProperties;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.vcode.Captcha;
import com.bzs.utils.vcode.GifCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by alwaysacc on 2019/04/15.
*/
@RestController
@RequestMapping("/account/info")
public class AccountInfoController {
    private static final String CODE_KEY = "_code";

    private Logger log=LoggerFactory.getLogger(this.getClass());
    @Resource
    private AccountInfoService accountInfoService;

    @Autowired
    private FebsProperties febsProperties;

    @PostMapping("/add")
    public Result add(AccountInfo accountInfo) {
        accountInfoService.save(accountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(String id) {
        return ResultGenerator.genSuccessResult(accountInfoService.findByLoginName(id));
    }

    @PostMapping("/update")
    public Result update(AccountInfo accountInfo) {
        accountInfoService.update(accountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        AccountInfo accountInfo = accountInfoService.findById(id);
        return ResultGenerator.genSuccessResult(accountInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AccountInfo> list = accountInfoService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @GetMapping(value = "gifCode")
    public void getGifCode(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");

            Captcha captcha = new GifCaptcha(
                    febsProperties.getValidateCode().getWidth(),
                    febsProperties.getValidateCode().getHeight(),
                    febsProperties.getValidateCode().getLength());
            HttpSession session = request.getSession(true);
            captcha.out(response.getOutputStream());
            session.removeAttribute(CODE_KEY);
            session.setAttribute(CODE_KEY, captcha.text().toLowerCase());
        } catch (Exception e) {
            log.error("图形验证码生成失败", e);
        }
    }

    @PostMapping("/testLogin")
    public Result testLogin(String userName, String password) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return ResultGenerator.genFailResult("登录账号或者密码为空");
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        String failMsg = "";
        if (!subject.isAuthenticated()) {//是否通过login()进行了身份验证
            // remembermMe记住密码
            //token.setRememberMe(true);
            try {
                subject.login(token);
                Map<String, Object> data = new HashMap<>();
                data.put("token", subject.getSession().getId());
                return ResultGenerator.genSuccessResult(data);
            } catch (UnknownAccountException e) {
                failMsg = "用户不存在";
            } catch (IncorrectCredentialsException e) {
                failMsg = "密码错误！";
            } catch (LockedAccountException e) {
                failMsg = "登录失败，该用户已被冻结";
            } catch (ExcessiveAttemptsException e) {
                failMsg = "登录失败次数过多";
            } catch (DisabledAccountException e) {
                failMsg = "帐号已被禁用";
            } catch (ExpiredCredentialsException e) {
                failMsg = "帐号已过期";
            } catch (UnauthorizedException e) {
                failMsg = "您没有得到相应的授权！";
            } catch (Exception e) {
                log.error("系统内部异常！！{}", e);
                failMsg="系统异常";
            }
            return ResultGenerator.genFailResult(failMsg);
        }
        return ResultGenerator.genSuccessResult("已经登录");
    }
    @RequestMapping(value="test1",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>test1(HttpServletRequest request){
      AccountInfo a=(AccountInfo)  SecurityUtils.getSubject().getPrincipal();
      Map<String,Object> map=new HashMap<>();
        if(a!=null){
            map.put("status","400");
            map.put("msg",a.getLoginName()+"已登录");
        }else{
            map.put("status","400");
            map.put("msg","还未登录");
        }
        return map;
    }
    @ApiOperation("获取所有账号以及获取账号下的第三方账号")
    @PostMapping("/getAccountAndThridAccount")
    public Result getAccountAndThridAccount(String accountId){
        return accountInfoService.getAccountAndThridAccount(accountId);
    }
}
