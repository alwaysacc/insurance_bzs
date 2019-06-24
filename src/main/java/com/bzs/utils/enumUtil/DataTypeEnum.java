package com.bzs.utils.enumUtil;

public enum DataTypeEnum {

    BYTE("BYTE"),
    SHORT("SHORT"),
    INTEGER("INTEGER"),
    LONG("LONG"),
    FLOAT("FLAOT"),
    DOUBLE("DOUBLE"),
    BOOLEAN("BOOLEAN"),
    CHAR("CHAR"),
    STRING("STRING"),
    DATE("DATE");
    private String value="";
    private DataTypeEnum(String value){
        this.value=value;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public static void main(String[] args) {
        for (DataTypeEnum enums:DataTypeEnum.values()) {

        }
    }
}
