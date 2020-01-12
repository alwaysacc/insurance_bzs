package com.bzs.controller;

import cn.hutool.core.util.StrUtil;
import com.bzs.cache.RedisAnnotation;
import com.bzs.dao.AccountInfoMapper;
import com.bzs.dao.IdCardImgMapper;
import com.bzs.dao.OrderInfoMapper;
import com.bzs.dao.QuoteInfoMapper;
import com.bzs.model.*;
import com.bzs.redis.RedisUtil;
import com.bzs.service.IdCardImgService;
import com.bzs.shiro.FebsProperties;
import com.bzs.utils.*;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.base64Util.Base64Util;
import com.bzs.utils.jsontobean.F;
import com.bzs.utils.juheUtil.JuHeHttpUtil;
import com.bzs.utils.redisConstant.RedisConstant;
import com.bzs.utils.saltEncryptionutil.SaltEncryptionUtil;
import com.bzs.utils.vcode.Captcha;
import com.bzs.utils.vcode.GifCaptcha;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.subject.Subject;
import org.apache.solr.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

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
    private static final String CODE = "CODE_LIST";
    @Autowired
    private FebsProperties febsProperties;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private AccountInfoMapper accountInfoMapper;
    @Resource
    private IdCardImgService idCardImgService;
    @Resource
    private IdCardImgMapper idCardImgMapper;

/*    @PostMapping("/hasBound")
    public Result hasBound(String openId) {
        Condition condition = new Condition(AccountInfo.class);
        condition.createCriteria().andCondition("openId=", openId);
        return ResultGenerator.genSuccessResult(accountInfoMapper.selectByCondition(condition));
    }*/

    @PostMapping("/hasBound")
    public Result genOpenId(String code) throws IOException {
        String appId = "wxe79b7f34a0a96a0f";
        String appSecret = "7ec07f9c12a4fa41b70604f4f9a363be";
        String openId = null;
        if (StrUtil.isNotBlank(code)) {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code");
            httpGet.setHeader("Accept", "application/json");
            CloseableHttpResponse httpResponse = client.execute(httpGet);
            String result = EntityUtils.toString(httpResponse.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(result);
            if (json.has("errcode")) {
                String errcode = json.get("errcode").asText();
                String errmsg = json.get("errmsg").asText();
                return ResultGenerator.genFailResult(errmsg);
            } else {
                openId = json.get("openid").asText();
                Map<String, Object> data = new HashMap<>();
                data.put("openId", openId);
                Condition condition = new Condition(AccountInfo.class);
                condition.createCriteria().andCondition("openId=", openId);
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setOpenId(openId);
                data.put("user", accountInfoMapper.selectOne(accountInfo));
                return ResultGenerator.genSuccessResult(data);
            }
        } else {
            return ResultGenerator.genFailResult("");
        }
    }

    @PostMapping("/getHomeInfo")
    public Result getHomeInfo() {
        return ResultGenerator.genSuccessResult(accountInfoService.getHomeInfo());
    }

    @PostMapping("/checkUserMoile")
    public Result checkUserMoile(String mobile) {
        return ResultGenerator.genSuccessResult(accountInfoService.checkUserMobile().contains(mobile));
    }

    @PostMapping("/checkUserLoginName")
    public Result checkUserLoginName(String loginName) {
        return ResultGenerator.genSuccessResult(accountInfoService.checkUserLoginName().contains(loginName));
    }

    @PostMapping("/add")
    public Result add(AccountInfo accountInfo) {
        accountInfo.setAccountId(UUIDS.getDateUUID());
        accountInfo.setLoginPwd(MD5Utils.encrypt(accountInfo.getLoginName().toLowerCase(), accountInfo.getLoginPwd()));
        accountInfo.setRoleId("3");
        accountInfo.setAccountState(0);
        HashSet codeList = new HashSet<>();
        int code = 0;
        if (redisUtil.hasKey(CODE)) {
            codeList = (HashSet) redisUtil.get(CODE);
        } else {
            codeList = accountInfoMapper.getAllCode();
        }
        boolean b = false;
        do {
            code = UUIDS.getCode();
            b = codeList.contains(code);
        } while (b);
        codeList.add(code);
        redisUtil.set(CODE, codeList, 720000);
        accountInfo.setInvitecode(code);
        accountInfo.setRoleName("业务员");
        accountInfoService.save(accountInfo);
        HashSet set;
        if (!redisUtil.hasKey(RedisConstant.USER_LOGIN_NAME_LIST)) {
            set = accountInfoMapper.getUserLoginName();
            redisUtil.set(RedisConstant.USER_LOGIN_NAME_LIST, set, 720000);
        } else {
            set = (HashSet) redisUtil.get(RedisConstant.USER_LOGIN_NAME_LIST);
            set.add(accountInfo.getLoginName());
            redisUtil.set(RedisConstant.USER_LOGIN_NAME_LIST, set, 720000);
        }
        HashSet set1;
        if (!redisUtil.hasKey(RedisConstant.USER_MOILE)) {
            set1 = accountInfoMapper.checkUserMobile();
            redisUtil.set(RedisConstant.USER_MOILE, set1, 720000);
        } else {
            set1 = (HashSet) redisUtil.get(RedisConstant.USER_MOILE);
            set1.add(accountInfo.getMobile());
            redisUtil.set(RedisConstant.USER_MOILE, set1, 720000);
        }
        return ResultGenerator.genSuccessResult(accountInfo);
    }

    @PostMapping("/registerForWX")
    public Result registerForWX(AccountInfo accountInfo) {
        return ResultGenerator.genSuccessResult(accountInfoService.registerForWX(accountInfo));
    }

    @PostMapping("/delete")
    public Result delete(String id) {
        return ResultGenerator.genSuccessResult(accountInfoService.findByLoginName(id));
    }

    @PostMapping("/updateUserIdCard")
    public Result updateUserIdCard(AccountInfo accountInfo) {
        accountInfoService.update(accountInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(String accountId, int accountState) {
        accountInfoMapper.updateAccountStat(accountId, accountState);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        AccountInfo accountInfo = accountInfoService.findById(id);
        return ResultGenerator.genSuccessResult(accountInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size, String userName, String mobile) {
        PageHelper.startPage(page, size);
      /*  Condition condition = new Condition(AccountInfo.class);
        condition.createCriteria().andCondition("delete_status ="+0);*/
        List list = accountInfoService.getUserListByAdmin(userName, mobile);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/updateAccount")
    public Result updateAccount(AccountInfo accountInfo) {
      /*  Condition condition = new Condition(AccountInfo.class);
        condition.createCriteria().andCondition("delete_status ="+0);*/
        return ResultGenerator.genSuccessResult(accountInfoService.updateAccount(accountInfo));
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
//        password  =SaltEncryptionUtil.getEncryptionByName(userName,password);
        password = MD5Utils.encrypt(userName.toLowerCase(), password);
        System.out.println("输入的密码MD5装换后" + password);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        String failMsg = "";
//        if (!subject.isAuthenticated()) {//是否通过login()进行了身份验证
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
//        }
//        return ResultGenerator.genSuccessResult("已经登录");
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
            map.put("status", "200");
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
    public Result getParentList(String id, Integer deep, String type, String isOwner, int accountState) {
        deep = 2;
        isOwner = "1";
        return accountInfoService.getParentOrChildList(id, deep, isOwner, type, accountState);
    }

    @ApiOperation("获取父节点账号2级")
    @PostMapping("/getParentLevel")
    public Result getParentLevel(String createBy) {
        return accountInfoService.getParentLevel(createBy);
    }

    @ApiOperation("更新余额和佣金和提成")
    @PostMapping("/updateMoney")
    public Result updateMoney(BigDecimal balanceTotal, BigDecimal commissionTotal, BigDecimal drawPercentageTotal, String accountId, Verification verification) {
        return accountInfoService.updateMoney(balanceTotal, commissionTotal, drawPercentageTotal, accountId, verification);
    }

    @ApiOperation("账号管理-添加账号")
    @PostMapping("/addOrUpdateAccount")
    public Result addOrUpdateAccountForMananger(AccountInfo accountInfo) {
        return accountInfoService.addOrUpdateAccountForMananger(accountInfo);
    }

    @ApiOperation("获取收益信息")
    @PostMapping("/getWithdraw")
    public Result getWithdraw(String accountId) {
        return ResultGenerator.genSuccessResult(accountInfoService.getWithdraw(accountId));
    }

    @ApiOperation("批量删除用户")
    @PostMapping("/deleteUser")
    public Result deleteUser(String[] accountId, int status) {
        return ResultGenerator.genSuccessResult(accountInfoService.deleteUser(accountId, status));
    }

    @ApiOperation("发送验证码")
    @PostMapping("/getCode")
    public Result getCode(String mobile) {
        return ResultGenerator.genSuccessResult(JuHeHttpUtil.getRequest(mobile));
    }

    @ApiOperation("实名认证")
    @PostMapping("/accountVerified")
    public Result accountVerified(@RequestParam(value = "file", required = false) MultipartFile file, int type, IdCardImg idCardImg) throws Exception {

        accountInfoService.accountVerified(file, type, idCardImg.getAccountId());
        if (StrUtil.isNotBlank(idCardImg.getBank())) {
            Condition condition = new Condition(IdCardImg.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("accountId", idCardImg.getAccountId());
            idCardImgMapper.updateByConditionSelective(idCardImg, condition);
        }
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation("找回密码")
    @PostMapping("/updatePassWord")
    public Result updatePassWord(AccountInfo accountInfo) {
        return ResultGenerator.genSuccessResult(accountInfoService.updatePassWord(accountInfo));
    }

    @PostMapping("/getUserNameAndId")
    public Result getUserNameAndId() {
        return ResultGenerator.genSuccessResult(accountInfoService.getUserNameAndId());
    }

    @PostMapping("/getUserNameList")
    public Result getUserNameList() {
        return ResultGenerator.genSuccessResult(accountInfoMapper.getUserNameList());
    }

    @ApiOperation("驳回实名认证")
    @PostMapping("/updateAccountVerifiedStat")
    public Result updateAccountVerifiedStat(String accountId, int verifiedStat,
                                            int id, String msg
    ) {
        return accountInfoService.updateAccountVerifiedStat(accountId, verifiedStat, id, msg);
    }

    @ApiOperation("验证实名认证")
    @PostMapping("/checkAccountVerified")
    public Result checkAccountVerified(IdCardImg idCardImg, String mobile, String name, String idCard) {
        return accountInfoService.checkAccountVerified(idCardImg, mobile, name, idCard);
    }

}
