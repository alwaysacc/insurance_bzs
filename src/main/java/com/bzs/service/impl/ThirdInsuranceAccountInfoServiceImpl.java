package com.bzs.service.impl;

import com.bzs.dao.ThirdInsuranceAccountInfoMapper;
import com.bzs.model.ThirdInsuranceAccountInfo;
import com.bzs.service.ThirdInsuranceAccountInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import com.bzs.utils.UUIDS;
import com.bzs.utils.commons.ObjectUtil;
import com.bzs.utils.enumUtil.InsuranceNameEnum;
import com.bzs.utils.enumUtil.InsuranceNameEnum2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/05/06.
 */
@Service
@Transactional
public class ThirdInsuranceAccountInfoServiceImpl extends AbstractService<ThirdInsuranceAccountInfo> implements ThirdInsuranceAccountInfoService {
   private static Logger logger=LoggerFactory.getLogger(ThirdInsuranceAccountInfoServiceImpl.class);
    @Resource
    private ThirdInsuranceAccountInfoMapper thirdInsuranceAccountInfoMapper;

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
    public Result addOrUpdate(ThirdInsuranceAccountInfo accountInfo, String type) {
       String msg="";
        if(StringUtils.isNotBlank(type)){
            if("0".equals(type))msg="添加";
            else if("1".equals(type))msg="修改";
        }
        //初始化后的对象不为null,根据属性值判断，属性值如果是 "" 则释为空
        //if(!ObjectUtil.isEmptyIncludeQuotationMark(accountInfo)){
        if(null!=accountInfo){
           String id= accountInfo.getThirdInsuranceId();
            if(StringUtils.isNotBlank(id)){
                msg="修改";
            }else{
                String uuid=UUIDS.getDateUUID();
                accountInfo.setThirdInsuranceId(uuid);
                msg="添加";
            }
            int result=  thirdInsuranceAccountInfoMapper.addOrUpdate(accountInfo);
            return  ResultGenerator.genSuccessResult(result,msg+"成功");
        }else {
            String msg2="参数异常,"+msg+"失败";
            return ResultGenerator.genFailResult(msg2);
        }

    }
}
