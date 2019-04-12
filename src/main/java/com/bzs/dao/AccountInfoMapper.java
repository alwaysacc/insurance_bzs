package com.bzs.dao;

import com.bzs.model.AccountInfo;
import com.bzs.utils.Mapper;

public interface AccountInfoMapper extends Mapper<AccountInfo> {
    String getRoleIdByAccountId(String account_id);
}