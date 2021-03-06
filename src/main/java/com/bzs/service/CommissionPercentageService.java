package com.bzs.service;
import com.bzs.model.CommissionPercentage;
import com.bzs.utils.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/12.
 */
public interface CommissionPercentageService extends Service<CommissionPercentage> {
    /**
     *
     * @param domain
     * @return
     */
    Map addOrUpdate(CommissionPercentage domain);
    CommissionPercentage get(CommissionPercentage domain);

    /**
     * 获取最新的数据对象
     * @return
     */
    CommissionPercentage getLastUpdateData();

    /**
     * 获取最新的数据对象id
     * @return
     */
    Long getId();

    /**
     * 条件查询
     * @param comm
     * @return
     */
    List<CommissionPercentage> select(CommissionPercentage comm);

    List getListAndUpdateBy();
}
