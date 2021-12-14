package io.dubai.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRechargeVO {

    private Long id;

    // 充币地址
    private String address;

    // 充币数量
    private BigDecimal amount;

    // 区块链转账hash
    private String txHash;

    // 区块高度
    private String txHeight;

    // 确认高度
    private String confirmHeight;

    // 币种名称
    private String coinName;

    // 1,已完成
    private Integer state;

    // 充币时间，单位毫秒
    private Long time;

}
