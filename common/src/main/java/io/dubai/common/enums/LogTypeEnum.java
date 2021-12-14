package io.dubai.common.enums;

import lombok.AllArgsConstructor;

/**
 * 明细方向
 */
@AllArgsConstructor
public enum LogTypeEnum {

    INCOME(1, "收入"),
    OUTLAY(2, "支出");


    public final Integer code;
    public final String desc;

}
