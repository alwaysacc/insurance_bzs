package com.bzs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bzs.dao.DrawCashMapper;
import com.bzs.model.DrawCash;
import com.bzs.service.CommissionPercentageService;
import com.bzs.service.DrawCashService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.UUIDS;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/13.
 */
@Service
@Transactional
public class DrawCashServiceImpl extends AbstractService<DrawCash> implements DrawCashService {
   private static Logger logger=LoggerFactory.getLogger(DrawCashServiceImpl.class);
    @Resource
    private DrawCashMapper drawCashMapper;
    @Resource
    private  CommissionPercentageService  commissionPercentageService;

    @Override
    public Map insertBatch(String list, String createBy) {
        Map map=new HashMap();
        if(StringUtils.isNotBlank(list)&&StringUtils.isNotBlank(createBy)){
           Long commissionPercentageId=commissionPercentageService.getId();
            List<String>  idLists= JSONArray.parseArray(list,String.class);
            String serialNo=UUIDS.getDateUUID();
            List <DrawCash>listDrawCash=new ArrayList<>();
            // 使用 lambda 表达式以及函数操作(functional operation)
            idLists.forEach((id) -> {
                DrawCash drawCash=new DrawCash(id,commissionPercentageId.intValue(),serialNo,createBy);
                listDrawCash.add(drawCash);
            });
            drawCashMapper.insertBatch(listDrawCash);

            map.put("code","200");
            map.put("msg","添加成功");
            map.put("data",listDrawCash);
        }else{
            map.put("code","400");
            map.put("msg","参数异常");
            map.put("data","");
        }
        return map;
    }
}
