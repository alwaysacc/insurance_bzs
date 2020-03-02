package com.bzs.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bzs.dao.AccountInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.service.AccountInfoService;
import com.bzs.utils.MD5Utils;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.jsontobean.C;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
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
    public Result login(String username,String password,String code,boolean rememberMe,String openCode) throws IOException {
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

        if (StrUtil.isNotBlank(openCode)){
            String appId = "wxe79b7f34a0a96a0f";
            String appSecret = "7ec07f9c12a4fa41b70604f4f9a363be";
            String openId = null;
            if (StrUtil.isNotBlank(openCode)) {
                CloseableHttpClient client = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + openCode + "&grant_type=authorization_code");
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
                }
            }
            Condition condition=new Condition(AccountInfo.class);
            Example.Criteria criteria=condition.createCriteria();
            criteria.andEqualTo("openId",openId);
            List<AccountInfo> list=accountInfoMapper.selectByCondition(condition);
            for (AccountInfo a:list) {
                a.setOpenId(null);
                accountInfoMapper.updateByPrimaryKey(a);
            }
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
