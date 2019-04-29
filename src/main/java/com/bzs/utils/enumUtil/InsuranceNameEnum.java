package com.bzs.utils.enumUtil;

/**
 * @program: insurance_bzs
 * @description: 枚举类型
 * @author: dengl
 * @create: 2019-04-17 10:01
 */
public enum InsuranceNameEnum {
    CPIC("太平洋保险",1L),
    PAIC("平安保险",2L),
    PICC("人民保险",4L)
    ;

    private  Long code;
    private String name;
    private InsuranceNameEnum(String name,Long code){
        this.name=name;
        this.code=code;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // 普通方法
    public static String getName(Long code) {
        for (InsuranceNameEnum c : InsuranceNameEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
