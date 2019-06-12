package com.bzs.service.impl;

import com.bzs.dao.CommissionPercentageMapper;
import com.bzs.model.CommissionPercentage;
import com.bzs.service.CommissionPercentageService;
import com.bzs.utils.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/12.
 */
@Service
@Transactional
public class CommissionPercentageServiceImpl extends AbstractService<CommissionPercentage> implements CommissionPercentageService {
    @Resource
    private CommissionPercentageMapper commissionPercentageMapper;

    @Override
    public Map<String, Object> add(String createBy, String accountId, String percent1, String percent2, String percent3) {
        Map<String, Object>map=new HashMap<>();
        if(StringUtils.isBlank(createBy)||StringUtils.isBlank(accountId)){
           map.put("code","400");
           map.put("msg","参数为空,添加失败");
           map.put("data",null);
           return  map ;
       }
        if(StringUtils.isBlank(percent1)||StringUtils.isBlank(percent2)||StringUtils.isBlank(percent3)){
            map.put("code","400");
            map.put("msg","三级拥挤比例必须同时设置");
            map.put("data",null);
            return  map ;
        }

        List<CommissionPercentage>list=new ArrayList<>();
        list.add(new CommissionPercentage(accountId,percent1,1,createBy));
        list.add(new CommissionPercentage(accountId,percent2,2,createBy));
        list.add(new CommissionPercentage(accountId,percent3,3,createBy));

        try{
            int result=commissionPercentageMapper.insertList(list);
            if(result>0){
                map.put("code","200");  map.put("msg","添加成功");
            }else{
                map.put("code","400");  map.put("msg","添加失败");
            }
            map.put("data",list);
        }catch (Exception e){
            map.put("code","400");
            map.put("msg","添加失败，出险异常");
            map.put("data",null);
        }
        return map;
    }
}
