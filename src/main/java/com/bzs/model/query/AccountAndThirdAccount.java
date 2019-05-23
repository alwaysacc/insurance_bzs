package com.bzs.model.query;

import com.bzs.model.AccountInfo;
import com.bzs.model.ThirdInsuranceAccountInfo;

import javax.persistence.Transient;
import java.util.List;

/**
 * @program: insurance_bzs
 * @description: 账号和第三方账号
 * @author: dengl
 * @create: 2019-05-14 10:39
 */
public class AccountAndThirdAccount extends AccountInfo {
    @Transient
    private List<ThirdInsuranceAccountInfo> thridAccounts;//第三方账号

    public List<ThirdInsuranceAccountInfo> getThridAccounts() {
        return thridAccounts;
    }

    public void setThridAccounts(List<ThirdInsuranceAccountInfo> thridAccounts) {
        this.thridAccounts = thridAccounts;
    }
}
