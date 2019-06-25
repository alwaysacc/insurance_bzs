package com.bzs.model.query;

import com.bzs.model.Admin;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-06-24 15:21
 */
public class AdminAndRole extends Admin {
    private String roleCode;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
