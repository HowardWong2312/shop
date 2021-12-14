package io.dubai.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(description = "用户钱包资产总额")
@Data
public class WalletAssetsTotalVO {

    @ApiModelProperty(value = "总金额")
    private BigDecimal total;

    @ApiModelProperty(value = "可用金额")
    private BigDecimal available;

    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freeze;

    @ApiModelProperty(value = "资产明细")
    private List<UserWalletAssetsVO> assets;


}
