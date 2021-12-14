package io.dubai.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletWithdrawVO {

    // 提币id
    private Long id;

    // 提币地址
    private String address;

    // 提币标签
    private String label;

    // 数量
    private BigDecimal amount;

    // 网络手续费
    private BigDecimal fee;

    // txHash
    private String txHash;

    // 币种
    private String coinName;

    // 用户账号
    private Integer memberId;

    // 汇出时间
    private Long time;

    // 状态(0：审核中，1 :已汇出，2：用户取消，3：审核不通过)
    private Integer status;

}
