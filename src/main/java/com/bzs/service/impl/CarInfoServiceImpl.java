package com.bzs.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bzs.dao.AccountInfoMapper;
import com.bzs.dao.CarInfoMapper;
import com.bzs.model.AccountInfo;
import com.bzs.model.CarInfo;
import com.bzs.model.query.CarInfoAndInsuranceInfo;
import com.bzs.service.CarInfoService;
import com.bzs.utils.*;
import com.bzs.utils.commons.ThirdAPI;
import com.bzs.utils.httpUtil.HttpClientUtil;
import com.bzs.utils.httpUtil.HttpResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
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
    public List getUserList(String accountId,String roleId,String salesman,String customerStatus,String plan,int selectType,int orderByDate) {
        //roleId= accountInfoMapper.getRoleIdByAccountId(accountId);
     /*   if (roleId.equals("管理员")){

        }else if (roleId.equals("管理员分账户")){

        }else if (roleId.equals("代理商")){

        }else{

        }*/

        String begDate="";
        String endDate="";
        if (selectType==1 || orderByDate==1){
            begDate=cn.hutool.core.date.DateUtil.formatDate(new Date());
            endDate=cn.hutool.core.date.DateUtil.formatDate(DateUtil.offsetDay(new Date(),40));
        }
        return carInfoMapper.getUserList(accountId,roleId,salesman,customerStatus,plan,begDate,endDate,orderByDate);
    }

    @Override
    public List searchUserList(String accountId, String roleId, String carNumber, String frameNumber, String customerName,
                               String customerTel, String lincenseOwner) {
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

    @Override
    public Result WX_GetNewVehicleInfo(String LicenseNo, String EngineNo, String CarVin, int IsNeedCarVin, String MoldName) {

        String param = "";
        if (IsNeedCarVin==0){
            param = ThirdAPI.BEFORE + "LicenseNo=" + LicenseNo +
                    "&IsNeedCarVin=" + IsNeedCarVin+"&MoldName=" + MoldName+ ThirdAPI.AFTER;
        }else{
            if (StringUtils.isNotBlank(CarVin))
                CarVin="0";
            if (StringUtils.isNotBlank(EngineNo))
                EngineNo="0";
            param = ThirdAPI.BEFORE + "LicenseNo=" + LicenseNo + "&EngineNo=" + EngineNo+"&CarVin=" + CarVin
                    +"&IsNeedCarVin=" + IsNeedCarVin+"&MoldName=" + MoldName+ ThirdAPI.AFTER;
        }
        String SecCode = MD5Utils.md5(param + "d7eb7d66997");
        param = param + "&SecCode=" + SecCode;
        param = param.replaceAll(" ", "%20");
        String url = ThirdAPI.GetNewVehicleInfo + param;
        HttpResult httpResult = null;
        try {
            httpResult = HttpClientUtil.doGet(url, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(httpResult);
        JSONObject object = JSONObject.parseObject(httpResult.getBody());
        if (httpResult.getCode()==200){
           if (object.getIntValue("BusinessStatus")==1){
               return ResultGenerator.genSuccessResult(object);
           }else{
               return ResultGenerator.genFailResult(object.getString("StatusMessage"));
           }
        }else{
            return ResultGenerator.genFailResult(object.getString("StatusMessage"));
        }
    }

    @Override
    public List getCarInfoQuote(String carInfoId, String createBy, String carNo, String vinNo, String isEnable, String isRenewSuccess,String queryTime) {
        return  carInfoMapper.getCarInfoAndQuoteList(carInfoId,carNo,createBy,vinNo,isEnable,isRenewSuccess,null,null);
    }

    @Override
    public List getCarInfoAndQuoteList(String carInfoId, String createBy, String carNo, String vinNo, String isEnable, String isRenewSuccess, String queryTime) {
        String startTime=null;
        String endTime=null;
        if (queryTime!=null && queryTime!=""){
            List<String> timeList= JSONArray.parseArray(queryTime).toJavaList(String.class);
            startTime=timeList.get(0);
            endTime=timeList.get(1);
        }
        return  carInfoMapper.getCarInfoAndInsurance(carInfoId,carNo,createBy,vinNo,isEnable,isRenewSuccess,startTime,endTime);
    }
}
