package com.bzs.service;
import com.bzs.model.PayAccount;
import com.bzs.utils.Service;

import java.util.List;


/**
 * Created by dl on 2019/06/13.
 */
public interface PayAccountService extends Service<PayAccount> {

    List getListById(String accountId);
}
