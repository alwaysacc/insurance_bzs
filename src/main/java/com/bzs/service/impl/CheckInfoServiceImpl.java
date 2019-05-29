package com.bzs.service.impl;

import com.bzs.dao.CheckInfoMapper;
import com.bzs.model.CheckInfo;
import com.bzs.service.CheckInfoService;
import com.bzs.utils.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by alwaysacc on 2019/04/12.
 */
@Service
@Transactional
public class CheckInfoServiceImpl extends AbstractService<CheckInfo> implements CheckInfoService {
    @Resource
    private CheckInfoMapper checkInfoMapper;

    @Override
    public Map updateOrAdd(CheckInfo checkInfo) {
        Map result = new HashedMap();
        int code = checkInfoMapper.updateOrAdd(checkInfo);
        String status = "400";
        String msg = "添加或者修改失败";
        if (code > 0) {
            status = "200";
            result.put("msg", "添加或者修改成功");
        }
        result.put("code", status);
        result.put("msg", msg);
        result.put("data", null);
        return result;
    }

    @Override
    public Map checkByCreateByOrCarInfoId(String createBy, String carInfoId,String carNo,String vinNo) {
        Map resultMap=new HashedMap();
        String msg="查询失败";
        String code="400";
        CheckInfo checkInfo=null;
        if(StringUtils.isNotBlank(createBy)||StringUtils.isNotBlank(carInfoId)){
            checkInfo=new CheckInfo();
            checkInfo.setCreateBy(createBy);
            checkInfo.setCarInfoId(carInfoId);
            checkInfo.setCarNo(carNo);
            checkInfo.setVinNo(vinNo);
            Map mapData=  this.checkByDifferConditions(checkInfo);
            code=(String)mapData.get("code");
            msg=(String)mapData.get("msg");
            if("200".equals(code)){
                checkInfo=(CheckInfo)mapData.get("data");
            }
        }else{
            msg="参数异常";
        }
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        resultMap.put("data",checkInfo);
        return resultMap;
    }

    @Override
    public Map checkByDifferConditions(CheckInfo checkInfo) {
        checkInfo=checkInfoMapper.checkByDifferConditions(checkInfo);
        Map resultMap=new HashedMap();
        String code="400";
        String msg="查询成功,但未获取数据信息";
        if(null!=checkInfo){
            code="200";
            msg="查询成功";
        }
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        resultMap.put("data",checkInfo);
        return resultMap;
    }

    @Override
    public Map getListByDifferConditions(CheckInfo checkInfo) {
        Map map=new HashedMap();
        String msg="获取失败";
        String code="400";
        if(null!=checkInfo){
            List list= checkInfoMapper.getListByDifferConditions(checkInfo);
            if(CollectionUtils.isNotEmpty(list)){
                code="200";
                msg="获取成功";
                map.put("data",list);
            }else{
                map.put("data",null);
            }
        }else{
            msg="参数异常";
            map.put("data",null);
        }
        map.put("code",code);
        map.put("msg",msg);
        return map;
    }
}
