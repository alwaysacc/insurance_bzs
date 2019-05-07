package com.bzs.service;
import com.bzs.model.ThirdInsuranceAccountDateInfo;
import com.bzs.utils.Result;
import com.bzs.utils.Service;


/**
 * Created by dl on 2019/05/06.
 */
public interface ThirdInsuranceAccountDateInfoService extends Service<ThirdInsuranceAccountDateInfo> {
public Result saveByProcedure(ThirdInsuranceAccountDateInfo info);
}
