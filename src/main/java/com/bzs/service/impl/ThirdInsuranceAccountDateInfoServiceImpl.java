package com.bzs.service.impl;

import com.bzs.dao.ThirdInsuranceAccountDateInfoMapper;
import com.bzs.model.ThirdInsuranceAccountDateInfo;
import com.bzs.service.ThirdInsuranceAccountDateInfoService;
import com.bzs.utils.AbstractService;
import com.bzs.utils.Result;
import com.bzs.utils.ResultGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;


/**
 * Created by dl on 2019/05/06.
 */
@Service
@Transactional
public class ThirdInsuranceAccountDateInfoServiceImpl extends AbstractService<ThirdInsuranceAccountDateInfo> implements ThirdInsuranceAccountDateInfoService {
    @Resource
    private ThirdInsuranceAccountDateInfoMapper thirdInsuranceAccountDateInfoMapper;

    @Override
    public Result saveByProcedure(ThirdInsuranceAccountDateInfo info) {
        try {
            if(null==info.getEnableDays()){
                info.setEnableDays(0);
            }
            if(null==info.getEnableMonths()){
                info.setEnableMonths(0);
            }
            if(null==info.getEnableYears()){
                info.setEnableYears(0);
            }
            thirdInsuranceAccountDateInfoMapper.saveByProcedure(info);
            return ResultGenerator.genSuccessResult();
        }catch(Exception e ){
          return   ResultGenerator.genFailResult("添加失败");
        }

    }
}
