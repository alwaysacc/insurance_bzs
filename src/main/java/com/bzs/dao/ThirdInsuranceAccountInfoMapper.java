package com.bzs.dao;

import com.bzs.model.ThirdInsuranceAccountInfo;
import com.bzs.utils.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ThirdInsuranceAccountInfoMapper extends Mapper<ThirdInsuranceAccountInfo> {
    /**
     * 根据id修改信息
     * @param accountInfo
     * @return
     */
    public int updateById(ThirdInsuranceAccountInfo accountInfo);
    /**
     * 根据天剑查询所有的账号信息
     * @param accountInfo
     * @return
     */
    public List queryConditions(ThirdInsuranceAccountInfo accountInfo);

    /**
     * 获取每个保司下可用的账号最新添加的账号
     * @param accountId
     * @param status
     * @return
     */
    public List findDifferSourceAccount(@Param("accountId") String accountId, @Param("status")String status);

    /**
     * 修改或添加
     * @param accountInfo
     * @return
     */
    int addOrUpdate(ThirdInsuranceAccountInfo accountInfo);

    /**
     * 查询分页列表
     * @param list
     * @param createBy
     * @return
     */
    int deleteBatch(@Param("list") List<String> list, @Param("createBy") String createBy);

    int deleteBatchAdmin(List<String> list);

    List getCrawlingAndAccountList(@Param("createBy") String createBy,@Param("code")String code);

    List getCrawlingAndAdminList();

    int deleteAccount(String accountId);
}