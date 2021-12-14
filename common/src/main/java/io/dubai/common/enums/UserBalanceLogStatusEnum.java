package io.dubai.common.enums;

import lombok.AllArgsConstructor;

/**
 * 资金明细类型
 */
@AllArgsConstructor
public enum UserBalanceLogStatusEnum {

    BALANCE_RECHARGE(1, "余额充值"),
    WITHDRAW_TO_BANK(2, "提现到银行卡"),
    PAID_FOR_SHOP_ORDER(3, "订单付款"),
    SHOP_ORDER_CANCELED(4, "订单取消"),
    SHOP_ORDER_INCOME(5, "电商收款"),
    CREDITS_EXCHANGE(6, "积分兑现"),
    PAID_FOR_CREDITS(7, "积分购买"),
    PAID_FOR_USER_LEVEL(8, "付款升级"),
    FAIL_WITHDRAW(9, "提现失败，资金退回"),
    RETURN_DELIVERY(10, "订单退货"),
    OTHER(0, "其它");


    public final Integer code;
    public final String desc;

}
