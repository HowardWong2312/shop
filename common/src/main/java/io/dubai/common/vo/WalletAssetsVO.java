package io.dubai.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletAssetsVO {

    private String address;

    private BigDecimal balance;

    private String coinName;

    private BigDecimal frozenBalance;

    private Long id;

    private Integer isLock;

    private Integer userId;

    private Integer toReleased;

    private String type;

    private Long version;

}
