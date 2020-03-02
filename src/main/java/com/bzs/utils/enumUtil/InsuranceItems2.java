package com.bzs.utils.enumUtil;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-04-16 16:49
 */
public enum InsuranceItems2 {
    A("机动车损失险", "A"),
    B("商业第三者责任险", "B"),
    G1("全车盗抢险", "G1"),//成功
    D3("车上人员责任险(驾驶员)", "D3"),
    D4("车上人员责任险(乘客)", "D4"),
    Q3("指定修理厂险", "Q3"),
    Z("自燃损失险", "Z"),
    F("玻璃单独破碎险", "F"),
    L("车身划痕损失险", "L"),
    X1("发动机涉水损失险", "X1"),
    R("精神损害抚慰金责任险", "R"),
    Z4("指定专修厂类型", "Z4"),
    Z2("修理期间费用补偿险", "Z2"),
    Z5("修理期间天数", "Z5"),
    Z3("机动车损失保险无法找到第三方特约险", "Z3"),


    MG1("全车盗抢险_不计免", "MG1"),
    ML("车身划痕损失险_不计免", "ML"),
    MA("机动车损失险_不计免", "MA"),
    MB("商业第三者责任险_不计免", "MB"),
    MD3("车上人员责任险(驾驶员)_不计免", "MD3"),
    MD4("车上人员责任险(乘客)_不计免", "MD4"),
    MZ("自燃损失险_不计免", "MZ"),
    MX1("发动机涉水损失险_不计免", "MX1"),
    MR("精神损害抚慰金责任险_不计免", "MR"),
    FORCEPREMIUM("交强险" , "FORCEPREMIUM");
    private String name;
    private String code;

    private InsuranceItems2(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // 普通方法
    public static String getName(String code) {
        for (InsuranceItems2 c : InsuranceItems2.values()) {
            if (c.getCode().equals(code)) {
                return c.name;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(InsuranceItems2.getName("MB"));
    }

}
