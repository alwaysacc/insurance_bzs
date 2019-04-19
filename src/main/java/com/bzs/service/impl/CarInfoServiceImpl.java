package com.bzs.service.impl;

import com.bzs.dao.AccountInfoMapper;
import com.bzs.dao.CarInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.model.CarInfo;
import com.bzs.service.CarInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultCode;
import com.bzs.utils.ResultGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public List getUserList(String accountId,String roleId,String salesman,String customerStatus) {
        //roleId= accountInfoMapper.getRoleIdByAccountId(accountId);
        if (roleId.equals("管理员")){

        }else if (roleId.equals("管理员分账户")){

        }else if (roleId.equals("代理商")){

        }else{

        }
        return carInfoMapper.getUserList(accountId,roleId,salesman,customerStatus);
    }

    @Override
    public List searchUserList(String accountId, String roleId, String carNumber, String frameNumber, String customerName, String customerTel) {
        return null;
    }

    @Override
    public int recoverUser(String[] carInfoId) {
        return 0;
    }

    @Override
    public Result getCarInfoIdInfo(String carNo, String vinNo, String operatorId) {
        if(StringUtils.isBlank(carNo)||StringUtils.isBlank(vinNo)){
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
    public Map<String, Object> getCarInfoIdByCarNoAndVinNo(String carNo, String vinNo, String operatorId) {
        Map<String,Object> result=new HashMap<>();

        if(StringUtils.isNotBlank(carNo)&&StringUtils.isNotBlank(vinNo)){
            CarInfo data= carInfoMapper.getCarInfoIdByCarNoAndVinNo(carNo,vinNo);
            if(data!=null){
                result.put("status","200");
                result.put("msg","获取成功");
                result.put("data",data);
            }else{
                result.put("status","1");
                result.put("msg","请求成功，但是未查询到数据");
                result.put("data",null);
            }
        }else{
            result.put("status","400");
            result.put("msg","参数异常");
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
}
