package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bzs.dao.AdminMapper;
import com.bzs.dao.ThirdInsuranceAccountInfoMapper;
import com.bzs.model.ThirdInsuranceAccountInfo;
import com.bzs.model.query.AdminAndRole;
import com.bzs.service.AdminRoleService;
import com.bzs.service.ThirdInsuranceAccountInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.UUIDS;
import com.bzs.utils.commons.ObjectUtil;
import com.bzs.utils.enumUtil.InsuranceNameEnum;
import com.bzs.utils.enumUtil.InsuranceNameEnum2;
import com.bzs.utils.stringUtil.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by dl on 2019/05/06.
 */
@Service
@Transactional
public class ThirdInsuranceAccountInfoServiceImpl extends AbstractService<ThirdInsuranceAccountInfo> implements ThirdInsuranceAccountInfoService {
   private static Logger logger=LoggerFactory.getLogger(ThirdInsuranceAccountInfoServiceImpl.class);
    @Resource
    private ThirdInsuranceAccountInfoMapper thirdInsuranceAccountInfoMapper;
    @Resource
    private AdminMapper adminMapper;

    @Override
    public Result updateById(ThirdInsuranceAccountInfo accountInfo) {
        String message="";
        if(null!=accountInfo){
            String id=accountInfo.getThirdInsuranceId();
            if(StringUtils.isNotBlank(id)){
             int result=   thirdInsuranceAccountInfoMapper.updateById(accountInfo);
                System.out.println("执行结果："+result);
                return ResultGenerator.genSuccessResult(result,"修改成功");
            }else{
                message="请选中修改项";
                return ResultGenerator.genFailResult(message);
            }

        }else{
            message="获取需要修改的信息失败" ;
            return ResultGenerator.genFailResult(message);
        }
    }

    @Override
    public List queryConditions(ThirdInsuranceAccountInfo accountInfo) {
        return thirdInsuranceAccountInfoMapper.queryConditions(accountInfo);
    }

    @Override
    public Map<String, Object> findEnbaleAccount(Long source,String status,String accountId) {
        ThirdInsuranceAccountInfo accountInfo=new ThirdInsuranceAccountInfo();
        Map<String, Object> result=new HashMap<String, Object>();
        String code="400";
        String msg="";
        if(null!=source){
            accountInfo.setAccountType(source+"");
            logger.info("1太保2平安4人保,当前值:"+source);
        }else{
            msg="请指定保险公司枚举值";
        }
        if(StringUtils.isNotBlank(status)&&!"1".equals(status)){
            accountInfo.setStatus(status);
        }else{
            accountInfo.setStatus("1");//可用的
        }
        if(StringUtils.isNotBlank(accountId)){
            accountInfo.setAccountId(accountId);
        }else{
            msg="未获取账号信息";
        }
        if(StringUtils.isNotBlank(msg)){//参数异常
            result.put("code",code);
            result.put("msg",msg);
            result.put("data",null);
            return result;
        }
        List list= thirdInsuranceAccountInfoMapper.queryConditions(accountInfo);
        if(CollectionUtils.isNotEmpty(list)){
            ThirdInsuranceAccountInfo data=(ThirdInsuranceAccountInfo)list.get(0);
            result.put("code","200");
            result.put("msg","获取成功,如果启用多个，则获取的是最新添加的账号");
            result.put("data",data);
            return result;
        }else{
            result.put("code",code);
            result.put("msg","未添加相关账号或未启用");
            result.put("data",null);
            return result;
        }
    }

    @Override
    public Map<String, Object> findDifferSourceAccount(String accountId, String status) {
        Map<String, Object> result=new HashMap<>();
        String msg="";
        String code="400";
        if(StringUtils.isBlank(accountId)){
            msg="参数为空，请指定账号";
        }
        if(StringUtils.isBlank(status)){
            status="1";
        }
        if(StringUtils.isNotBlank(msg)){
            result.put("code",code);
            result.put("msg",msg);
            result.put("data",null);
            return  result;
        }
        List list= thirdInsuranceAccountInfoMapper.findDifferSourceAccount(accountId,status);
        if(CollectionUtils.isNotEmpty(list)){
            result.put("code","200");
            result.put("msg","获取成功");
            result.put("data",list);
            return result;
        }else{
            result.put("code",code);
            result.put("msg","未查询到");
            result.put("data",null);
            return result;
        }
    }

    @Override
    public Result addOrUpdate(ThirdInsuranceAccountInfo accountInfo, String createBy) {
        if(StringUtils.isBlank(createBy)){
            return  ResultGenerator.genFailResult("账号信息不能为空");
        }
        AdminAndRole ar= adminMapper.adminAndRoleByAdminId(Integer.valueOf(createBy));
        if(ar!=null){
            String  code=ar.getRoleCode();
            if(StringUtils.isBlank(code)||!"SADMIN".equals(code)||!"CADMIN".equals(code)){
                return  ResultGenerator.genFailResult("未获取权限");
            }
        }else{
            return  ResultGenerator.genFailResult("无查询到次账号信息");
        }
       String msg="";
        //初始化后的对象不为null,根据属性值判断，属性值如果是 "" 则释为空
        //if(!ObjectUtil.isEmptyIncludeQuotationMark(accountInfo)){
        if(null!=accountInfo){
           String id= accountInfo.getThirdInsuranceId();
            if(StringUtils.isNotBlank(id)){
                msg="修改";
            }else{
                String uuid=UUIDS.getDateUUID();
                accountInfo.setThirdInsuranceId(uuid);
              /*  Subject subject = SecurityUtils.getSubject();
                boolean b= subject.hasRole("SADMIN");
                if(!b){
                     accountInfo.setLevel("1");
                }*/
                msg="添加";
            }
            int result=  thirdInsuranceAccountInfoMapper.addOrUpdate(accountInfo);
            if(result>1){//更新
                return  ResultGenerator.genSuccessResult(result,"修改成功");
            }else if(result>0){//添加
                return  ResultGenerator.genSuccessResult(result,"添加成功");
            }else{
                return  ResultGenerator.genSuccessResult(result,msg+"失败");
            }
        }else {
            String msg2="参数异常,"+msg+"失败";
            return ResultGenerator.genFailResult(msg2);
        }
    }

    @Override
    public List<ThirdInsuranceAccountInfo> select(ThirdInsuranceAccountInfo thirdInsuranceAccountInfo) {
       if(null!=thirdInsuranceAccountInfo){
           String createBy=thirdInsuranceAccountInfo.getCreateId();
           if(StringUtils.isNotBlank(createBy)){
               Subject subject = SecurityUtils.getSubject();
               boolean b= subject.hasRole("SADMIN");
               if(b){
                   logger.info("是否超级管理员"+b);
                   createBy=null;
               }
           }
       }
        return this.thirdInsuranceAccountInfoMapper.select(thirdInsuranceAccountInfo);
    }

    @Override
    public Result deleteBatch(String ids, String createBy) {
        if(StringUtils.isNotBlank(ids)){
            List<String> list= JSONArray.parseArray(ids).toJavaList(String.class);
            Subject subject = SecurityUtils.getSubject();
            boolean b= subject.hasRole("SADMIN");
            if(b){
                logger.info("是否超级管理员"+b);
                createBy=null;
            }else{
                if(StringUtils.isBlank(createBy)){
                    return ResultGenerator.genFailResult("删除失败");
                }
            }
            int result=  this.thirdInsuranceAccountInfoMapper.deleteBatch(list,createBy);
            if(result>0){
                return  ResultGenerator.genSuccessResult(result,"删除成功");
            }else{
                return ResultGenerator.genFailResult("删除失败");
            }
        }
        return ResultGenerator.genFailResult("参数为空，删除失败");
    }

    @Override
    public List<ThirdInsuranceAccountInfo> selectByCreateBy(String crateBy) {
        ThirdInsuranceAccountInfo data=new ThirdInsuranceAccountInfo();
        data.setCreateId(crateBy);
        return this.select(data);
    }

    @Override
    public List getCrawlingAndAdminList(String createBy) {
        if(StringUtils.isBlank(createBy)){
            return null;
        }
        List list=  thirdInsuranceAccountInfoMapper.getCrawlingAndAccountList(createBy,null);
        return list;
        /*AdminAndRole ar= adminMapper.adminAndRoleByAdminId(Integer.valueOf(createBy));
        if(ar!=null){
            String  code=ar.getRoleCode();
            if(StringUtils.isBlank(code)){
                return null;
            }else{
                if("SADMIN".equals(code)||"CADMIN".equals(code)){
                    List list=  thirdInsuranceAccountInfoMapper.getCrawlingAndAccountList(null,null);
               return  list;
                }else if("CRAWLING".equalsIgnoreCase(code)){
                    List list=  thirdInsuranceAccountInfoMapper.getCrawlingAndAccountList(createBy,code);
                    return  list;
                }else{
                    return null;
                }

            }
        }else{
            return null;
        }*/


    }

    @Override
    public List getCrawlingAndAdminList() {
        return thirdInsuranceAccountInfoMapper.getCrawlingAndAdminList();
    }

    @Override
    public int deleteAccount(String accountId) {
        return thirdInsuranceAccountInfoMapper.deleteAccount(accountId);
    }
}
