package com.bzs.model.router;

import lombok.Data;

import java.util.List;

@Data
public class Parent {
    private String name;
    private String path;
    private String redirect;
    private String component;
    private boolean alwaysShow;
    private RouterMeta meta;
    private List<Children> children;
}
