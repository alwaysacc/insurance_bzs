package com.bzs.service.impl;

import com.bzs.dao.AccountInfoMapper;
import com.bzs.dao.CarInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.model.CarInfo;
import com.bzs.model.query.CarInfoAndInsuranceInfo;
import com.bzs.service.CarInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultCode;
import com.bzs.utils.ResultGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/10.
 */
@Service
@Transactional
public class CarInfoServiceImpl extends AbstractService<CarInfo> implements CarInfoService {
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Override
    public List getUserList(String accountId,String roleId,String salesman,String customerStatus,String plan) {
        //roleId= accountInfoMapper.getRoleIdByAccountId(accountId);
        if (roleId.equals("管理员")){

        }else if (roleId.equals("管理员分账户")){

        }else if (roleId.equals("代理商")){

        }else{

        }
        return carInfoMapper.getUserList(accountId,roleId,salesman,customerStatus,plan);
    }

    @Override
    public List searchUserList(String accountId, String roleId, String carNumber, String frameNumber, String customerName, String customerTel, String lincenseOwner) {
        return carInfoMapper.searchUserList(accountId,roleId,carNumber,frameNumber,customerName,customerTel,lincenseOwner);
    }

    @Override
    public List getRecoverUser(String accountId, String roleId) {
        return carInfoMapper.getRecoverUser(accountId,roleId);
    }

    @Override
    public int recoverUser(String[] carInfoId,int status) {
        return carInfoMapper.recoverUser(carInfoId,status);
    }

    @Override
    public Result getCarInfoIdInfo(String carNo, String vinNo, String operatorId) {
        if(StringUtils.isBlank(carNo)&&StringUtils.isBlank(vinNo)){
           return ResultGenerator.genFailResult("车牌号和车架号不能同时为空");
        }
        CarInfo carInfo=new CarInfo();
        carInfo.setCreatedBy(operatorId);
        carInfo.setCarNumber(carNo);
        carInfo.setFrameNumber(vinNo);
        List list=carInfoMapper.findOneBy(carInfo);
        if(CollectionUtils.isEmpty(list)){
            return ResultGenerator.gen("查询成功,返回值为空",list, ResultCode.SUCCESS_NULL);//
        }else{
            return ResultGenerator.genSuccessResult(list);
        }

    }

    @Override
    public Map userDetail(String carInfoId) {
        Condition condition=new Condition(CarInfo.class);
        return null;
    }

    @Override
    public Map<String, Object> getCarInfoIdByCarNoOrVinNo(String carNo, String vinNo, String operatorId) {
        Map<String,Object> result=new HashMap<>();
            List<CarInfo> list= carInfoMapper.getCarInfoIdByCarNoOrVinNo(carNo,vinNo);
            if(CollectionUtils.isNotEmpty(list)){
                result.put("status","200");
                result.put("msg","获取成功");
                result.put("data",list);
            }else{
                result.put("status","1");
                result.put("msg","请求成功，但是未查询到数据");
                result.put("data",null);
            }
        return result;
    }

    @Override
    public Map<String, Object> insertOrUpdate(CarInfo carInfo) {
       int code= carInfoMapper.insertOrUpdate(carInfo);
        Map<String, Object> result=new HashMap<>();
       if(code>0){
           result.put("status","200");
           result.put("msg","添加或者修改成功");
           result.put("data",null);
       }else{
           result.put("status","400");
           result.put("msg","添加或者修改失败");
           result.put("data",null);
       }
        return result;
    }

    @Override
    public Map<String, Object> getCarInfoAndInsurance(String carInfoId, String createBy,String carNo,String vinNo,String isEable,String isRenewSuccess) {
        CarInfo carInfo=new CarInfo();
        carInfo.setCarInfoId(carInfoId);
        carInfo.setCreatedBy(createBy);
        carInfo.setCarNumber(carNo);
        carInfo.setFrameNumber(vinNo);
        carInfo.setIsEnable(isEable);
        carInfo.setIsRenewSuccess(isRenewSuccess);
        Map<String, Object> result=new HashMap<>();
       List<CarInfoAndInsuranceInfo>  list=carInfoMapper.getCarInfoAndInsurance(carInfo);
       if(CollectionUtils.isNotEmpty(list)){
            result.put("code","200");
            result.put("msg","获取成功");
            result.put("data",list);
        }else{
            result.put("code","400");
            result.put("msg","获取失败");
            result.put("data",list);
        }
        return result;
    }

    @Override
    public Map updateBatchIsEnable(List ids, String isEnable) {
        Map<String,Object> resultMap=new HashedMap();
        String code="400";
        String msg="获取";
        int result=-1;
        if(CollectionUtils.isNotEmpty(ids)&&StringUtils.isNotBlank(isEnable)){
            code="200";
            msg+="成功";
            result= carInfoMapper.updateBatchIsEnable(ids,isEnable);
        }else{
            msg+="失败";
        }
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        resultMap.put("data",result);
        return resultMap;
    }
}
