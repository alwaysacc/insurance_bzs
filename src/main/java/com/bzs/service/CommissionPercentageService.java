package com.bzs.service;
import com.bzs.model.CommissionPercentage;
import com.bzs.utils.Service;

import java.util.Map;


/**
 * Created by dl on 2019/06/12.
 */
public interface CommissionPercentageService extends Service<CommissionPercentage> {
    /**
     * 给账号添加佣金比例
     * @param createBy
     * @param accountId
     * @param percent1
     * @param percent2
     * @param percent3
     * @return
     */
    Map<String,Object> add(String createBy, String accountId, String percent1, String percent2, String percent3 );
}
