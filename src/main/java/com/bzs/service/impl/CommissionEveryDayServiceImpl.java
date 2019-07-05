package com.bzs.service.impl;

import com.bzs.dao.CommissionEveryDayMapper;
import com.bzs.model.CommissionEveryDay;
import com.bzs.service.CommissionEveryDayService;
import com.bzs.utils.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by dl on 2019/07/03.
 */
@Service
@Transactional
public class CommissionEveryDayServiceImpl extends AbstractService<CommissionEveryDay> implements CommissionEveryDayService {
    @Resource
    private CommissionEveryDayMapper commissionEveryDayMapper;

}
