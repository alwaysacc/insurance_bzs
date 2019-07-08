package com.bzs.model.router;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

@Data
public class Children extends VueRouter<T> {

    private String name;
    private String path;
    private String component;
    private RouterMeta meta;
}
