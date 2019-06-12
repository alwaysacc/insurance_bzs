package com.bzs.model.query;

import com.bzs.model.OrderInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: insurance_bzs
 * @description: 订单和账号信息
 * @author: dengl
 * @create: 2019-06-12 13:29
 */
@Data
public class OrderAndAccount extends OrderInfo {
    private String accountId;//账号id
    private String userName;//用户名称
    private Integer pLevel;//一级等级
    private Integer level;//等级
    private Integer inviteCodeLevel;//本身邀请码等级
    private String status;//订单提现状态
    private BigDecimal bizTotal;//商业险总额
    private String carNo;//车牌号
    private String vinNo;//车架号

}
