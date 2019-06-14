package com.bzs.dao;

import com.bzs.model.PayAccount;
import com.bzs.utils.Mapper;

import java.util.List;

public interface PayAccountMapper extends Mapper<PayAccount> {
    List getListById(String accountId);
}