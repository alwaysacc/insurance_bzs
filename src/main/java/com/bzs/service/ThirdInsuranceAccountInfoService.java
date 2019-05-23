package com.bzs.service;
import com.bzs.model.ThirdInsuranceAccountInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;
import com.bzs.utils.enumUtil.InsuranceNameEnum;
import com.bzs.utils.enumUtil.InsuranceNameEnum2;

import java.util.List;
import java.util.Map;


/**
 * Created by dl on 2019/05/06.
 */
public interface ThirdInsuranceAccountInfoService extends Service<ThirdInsuranceAccountInfo> {
    /**
     * 根据id修改信息 为空不修改
     * @param thirdInsuranceAccountInfo
     * @return
     */

    public Result updateById(ThirdInsuranceAccountInfo thirdInsuranceAccountInfo);

    /**
     * 根据条件查询所有的保险账号信息
     * @param thirdInsuranceAccountInfo
     * @return
     */
    public List queryConditions(ThirdInsuranceAccountInfo thirdInsuranceAccountInfo);

    /**
     * @description 获取账号下可用的某个保险公司的账号,若存在多个返回最新添加的
     * @param source 账号类型比如1太保、2平安、4人保
     * @param status  默认"2"
     * @param accountId 账号
     * @return
     */
    public Map<String,Object> findEnbaleAccount(Long source,String status,String accountId);

    /**
     * 获取指定账号下的每家保险公司的可用账号
     * @param accountId
     * @param status
     * @return
     */
    public Map<String,Object>findDifferSourceAccount(String accountId,String status);

    /**
     * 添加或修改
     * @param accountInfo
     * @param type 0添加1修改 空不反回
     * @return
     */
    Result addOrUpdate(ThirdInsuranceAccountInfo accountInfo, String type);


}
