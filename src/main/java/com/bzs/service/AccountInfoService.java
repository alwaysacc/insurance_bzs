package com.bzs.service;
import com.bzs.model.AccountInfo;
import com.bzs.utils.Service;


/**
 * Created by alwaysacc on 2019/04/11.
 */
public interface AccountInfoService extends Service<AccountInfo> {
    String getRoleIdByAccountId(String account_id);
}
