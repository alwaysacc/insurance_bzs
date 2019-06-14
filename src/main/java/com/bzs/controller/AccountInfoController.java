package com.bzs.controller;

import com.bzs.redis.RedisUtil;
import com.bzs.shiro.FebsProperties;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.UUIDS;
import com.bzs.utils.saltEncryptionutil.SaltEncryptionUtil;
import com.bzs.utils.vcode.Captcha;
import com.bzs.utils.vcode.GifCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    private AccountInfoService accountInfoService;

    @Autowired
    private FebsProperties febsProperties;
    @Autowired
    private RedisUtil redisUtil;


    @PostMapping("/add")
    public Result add(AccountInfo accountInfo) {
        accountInfo.setAccountId(UUIDS.getDateUUID());
        accountInfo.setLoginPwd(MD5Utils.encrypt(accountInfo.getLoginName().toLowerCase(), accountInfo.getLoginPwd()));
        accountInfo.setRoleId("3");
        accountInfo.setAccountState("0");
        accountInfoService.save(accountInfo);
        return ResultGenerator.genSuccessResult(accountInfo);
    }

    @PostMapping("/registerForWX")
    public Result registerForWX(AccountInfo accountInfo) {
        return ResultGenerator.genSuccessResult( accountInfoService.registerForWX(accountInfo));
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
     //   redisUtil.set("accountInfo", userName);
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return ResultGenerator.genFailResult("登录账号或者密码为空");
        }
        Subject subject = SecurityUtils.getSubject();
        //password  =SaltEncryptionUtil.getEncryptionByName(userName,password);
        password = MD5Utils.encrypt(userName.toLowerCase(), password);
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
                failMsg = "系统异常";
            }
            return ResultGenerator.genFailResult(failMsg);
        }
        return ResultGenerator.genSuccessResult("已经登录");
    }

    @RequestMapping(value = "test1", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> test1(HttpServletRequest request) {
      // System.out.println(redisUtil.get("accountInfo"));
        AccountInfo a = (AccountInfo) SecurityUtils.getSubject().getPrincipal();
/*
        List<Map<String,Object>> queueMap=(List<Map<String,Object>>) redisUtil.get("queue");
        if(CollectionUtils.isNotEmpty(queueMap)){
            for (int i=0;i<queueMap.size();i++ ){
                if(CollectionUtils.isNotEmpty((List)queueMap.get(i))){
                    Map<String,Object> map=queueMap.get(i);
                    for (Map.Entry<String,Object> entry : map.entrySet()) {
                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    }
                }

            }

        }*/
        Map<String, Object> map = new HashMap<>();
        if (a != null) {
            map.put("status", "400");
            map.put("msg", a.getLoginName() + "已登录");
        } else {
            map.put("status", "400");
            map.put("msg", "还未登录");
        }
        return map;
    }

    @ApiOperation("获取所有账号以及获取账号下的第三方账号")
    @PostMapping("/getAccountAndThridAccount")
    public Result getAccountAndThridAccount(String accountId) {
        return accountInfoService.getAccountAndThridAccount(accountId);
    }

    /**
     * @param accountInfo
     * @param type        0 添加1修改
     * @return
     */
    @ApiOperation("插入或更新")
    @PostMapping("/insertOrUpdate")
    public Result insertOrUpdate(AccountInfo accountInfo, String type) {
        return accountInfoService.insertOrUpdate(accountInfo, type);
    }

    @PostMapping("/getUserList")
    public Result getUserList(String roleId, String accountId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        PageInfo pageInfo = new PageInfo(accountInfoService.getUserList(roleId, accountId));
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @ApiOperation("获取父节点账号")
    @PostMapping("/getParentList")
    /**
     * 获取父节点或者子节点
     * @param id
     * @param deep 深度 默认1 ，1的时候加上本身会查询两级
     * @param isOwner 默认0包括自己在内1不包括
     * @param type 默认0子节点1父节点
     * @return
     */
    public Result getParentList(String id,Integer deep,String type,String isOwner,int accountState){
        deep=2;
        isOwner="1";
      return accountInfoService.getParentOrChildList(id,deep,isOwner,type,accountState);
    }
    @ApiOperation("获取父节点账号2级")
    @PostMapping("/getParentLevel")
    public Result getParentLevel(String createBy){
       return accountInfoService.getParentLevel(createBy);
    }

    @ApiOperation("更新余额和佣金和提成")
    @PostMapping("/updateMoney")
    public Result updateMoney(BigDecimal balanceTotal,BigDecimal commissionTotal,BigDecimal drawPercentageTotal,String accountId){
        return accountInfoService.updateMoney(balanceTotal,commissionTotal,drawPercentageTotal,accountId);
    }
    @ApiOperation("获取收益信息")
    @PostMapping("/getWithdraw")
    public Result getWithdraw(String accountId){
        return ResultGenerator.genSuccessResult(accountInfoService.getWithdraw(accountId));
    }
}
