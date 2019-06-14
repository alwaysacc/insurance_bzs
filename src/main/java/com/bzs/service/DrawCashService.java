package com.bzs.service;
import com.bzs.model.DrawCash;
import com.bzs.utils.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/06/13.
 */
public interface DrawCashService extends Service<DrawCash> {
    /**
     * 批量添加
     * @param list orderId组成的josn字符串
     * @param createBy 添加人
     * @return
     */
    Map insertBatch(String list,String createBy);

    List getDrawCashList(String incomePerson,int type);
}
