package com.bzs.service.impl;

import com.bzs.dao.CommissionPercentageMapper;
import com.bzs.model.CommissionPercentage;
import com.bzs.model.TMenu;
import com.bzs.service.CommissionPercentageService;
import com.bzs.utils.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/12.
 */
@Service
@Transactional
public class CommissionPercentageServiceImpl extends AbstractService<CommissionPercentage> implements CommissionPercentageService {
    private  static Logger logger=LoggerFactory.getLogger(CommissionPercentageServiceImpl.class);
    @Resource
    private CommissionPercentageMapper commissionPercentageMapper;

    @Override
    public Map addOrUpdate(CommissionPercentage domain) {
        Map<String,Object>map=new HashMap<String, Object>();
        try {
            int result = commissionPercentageMapper.addOrUpdate(domain);
            map.put("data",domain);
            if(result>1){//2更新
                map.put("code","200");
                map.put("msg","更新成功");
            }else if(result>0){//添加
                map.put("code","200");
                map.put("msg","添加成功");
                List<CommissionPercentage> list=  this.commissionPercentageMapper.select(new CommissionPercentage(domain.getSource(),"1"));
                if(CollectionUtils.isNotEmpty(list)){
                    list.forEach((item)->{
                       if(item.getId()!=domain.getId()) {
                           CommissionPercentage comm= new CommissionPercentage();
                           comm.setId(item.getId());
                           comm.setStatus("0");
                           commissionPercentageMapper.addOrUpdate(comm);
                       }
                    });
                }
            }else{
                map.put("code","400");
                map.put("msg","添加或更新失败");
            }
        }catch (Exception e){
            logger.error("添加或更新异常",e);
            map.put("code","400");
            map.put("msg","添加或更新异常");
            map.put("data","");
        }

        return map;
    }
    @Override
    public CommissionPercentage get(CommissionPercentage domain) {
        Condition example=new Condition(CommissionPercentage.class);
        Example.Criteria  criteria= example.createCriteria();
        example.setOrderByClause("create_time");
        List<CommissionPercentage> menus = mapper.selectByCondition(example);
        if(CollectionUtils.isNotEmpty(menus)){
            return menus.get(0);
        }else{
            return null;
        }

    }

    @Override
    public CommissionPercentage getLastUpdateData() {
        CommissionPercentage data=commissionPercentageMapper.getLastUpdateData();
        return data;
    }

    @Override
    public Long getId() {
        CommissionPercentage data=commissionPercentageMapper.getLastUpdateData();
        if(null!=data){
            return data.getId();
        }
        return null;
    }

    @Override
    public List<CommissionPercentage> select(CommissionPercentage comm) {
        return commissionPercentageMapper.select(comm);
    }
}
