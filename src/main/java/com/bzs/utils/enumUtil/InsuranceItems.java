package com.bzs.utils.enumUtil;

/**
 * @program: insurance_bzs
 * @description: 保险项
 * @author: dengl
 * @create: 2019-04-16 16:16
 */
public enum InsuranceItems {
    A("机动车损失险"),
    B("商业第三者责任险"),
    G1("全车盗抢险"),//成功
    D3("车上人员责任险(驾驶员)"),
    D4("车上人员责任险(乘客)"),
    Q3("指定修理厂险"),
    Z("自燃损失险"),
    F("玻璃单独破碎险"),
    L("车身划痕损失险"),
    X1("发动机涉水损失险"),
    R("精神损害抚慰金责任险"),
    Z2("修理期间费用补偿险"),
    Z3("机动车损失保险无法找到第三方特约险"),
    MG1("全车盗抢险_不计免"),
    ML("车身划痕损失险_不计免"),
    MA("机动车损失险_不计免"),
    MB("商业第三者责任险_不计免"),
    MD3("车上人员责任险(驾驶员)_不计免"),
    MD4("车上人员责任险(乘客)_不计免"),
    MZ("自燃损失险_不计免"),
    MX1("发动机涉水损失险_不计免"),
    MR("精神损害抚慰金责任险_不计免"),
    FORCEPREMIUM("交强险");
    private final String code;
    InsuranceItems(String code) {
        this.code = code;
    }
    public String  code() {
        return code;
    }

}
