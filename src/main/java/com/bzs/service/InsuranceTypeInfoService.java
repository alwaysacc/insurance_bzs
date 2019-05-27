package com.bzs.service;

import com.bzs.model.InsuranceTypeInfo;
import com.bzs.utils.Service;

import java.util.List;


/**
 * Created by alwaysacc on 2019/04/12.
 */
public interface InsuranceTypeInfoService extends Service<InsuranceTypeInfo> {
    /**
     * 批量插入
     * @param list
     * @return 返回值插入数目
     */
    public int insertBatch(List<InsuranceTypeInfo> list);

    /**
     * 批量插入或者更新
     * @param info
     * @return
     */
    Boolean  batchInsertOrUpdateList(List <InsuranceTypeInfo> info);

}
