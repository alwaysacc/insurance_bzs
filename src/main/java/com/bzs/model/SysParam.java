package com.bzs.model;

import javax.persistence.*;

@Table(name = "sys_param")
public class SysParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String key;

    /**
     * 1 是 2否
     */
    private String value;

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
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取1 是 2否
     *
     * @return value - 1 是 2否
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置1 是 2否
     *
     * @param value 1 是 2否
     */
    public void setValue(String value) {
        this.value = value;
    }
}