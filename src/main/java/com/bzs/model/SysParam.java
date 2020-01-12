package com.bzs.model;

import javax.persistence.*;

@Table(name = "sys_param")
public class SysParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "param_key")
    private String paramKey;

    /**
     * 1 是 2否
     */
    @Column(name = "param_value")
    private String paramValue;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return param_key
     */
    public String getParamKey() {
        return paramKey;
    }

    /**
     * @param paramKey
     */
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * 获取1 是 2否
     *
     * @return param_value - 1 是 2否
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * 设置1 是 2否
     *
     * @param paramValue 1 是 2否
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}