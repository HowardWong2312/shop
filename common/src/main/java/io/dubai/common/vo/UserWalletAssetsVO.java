package io.dubai.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(description = "用户钱包资产明细")
@Data
public class UserWalletAssetsVO {

    @ApiModelProperty(value = "币种id")
    private Integer symbolId;

    @ApiModelProperty(value = "币种代码")
    private String symbol;

    @ApiModelProperty(value = "在哪个交易区显示(1,币币显示；2,法币显示;3,全部都显示;4,合约显示;5,钱包显示；6，仅币币法币；7，仅币币合约；8，仅币币钱包；9，仅法币合约；10，仅法币钱包；11，仅合约钱包；12，币币法币合约；13，币币法币钱包；14，币币合约钱包；15，法币合约钱包)")
    private Integer tradeArea;

    @ApiModelProperty(value = "充币状态（1，可充币；0，禁止充币）")
    private Integer canDeposit;

    @ApiModelProperty(value = "提币状态（1，可提币；0，禁止提币）")
    private Integer canWithdraw;

    @ApiModelProperty(value = "链分类(1,BTC类;2,ETH类;3,EOS类;4,其它)")
    private Integer symbolCategory;

    @ApiModelProperty(value = "总余额")
    private BigDecimal total;

    @ApiModelProperty(value = "可用余额")
    private BigDecimal available;

    @ApiModelProperty(value = "冻结余额")
    private BigDecimal frozen;

    @ApiModelProperty(value = "充币地址")
    private String depositAddress;

    @ApiModelProperty(value = "冻结余额估值")
    private BigDecimal frozenAppraisement;

    @ApiModelProperty(value = "可用余额估值")
    private BigDecimal appraisement;

    @ApiModelProperty(value = "总余额估值")
    private BigDecimal totalAppraisement;

    @ApiModelProperty(value = "充币二维码")
    private String imgQRCode;

    @ApiModelProperty(value = "24小时可提币额度")
    private BigDecimal availableWithdrawal;

    @ApiModelProperty(value = "已用提币额度")
    private BigDecimal hasWithdrawal;

    @ApiModelProperty(value = "地址标签(eos类别资产，充值、提币需要标签)")
    private String label;

}
