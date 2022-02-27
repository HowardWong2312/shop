package io.dubai.modules.other.query;

import io.dubai.common.utils.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("支付方式查询参数")
public class PaymentQuery extends Query {

    @ApiModelProperty("状态")
    private Integer status = 1;

    @ApiModelProperty("语言ID")
    private String languageId;

    @ApiModelProperty("是否收款方式")
    private Integer isWithdraw;

    @ApiModelProperty("是否充值方式")
    private Integer isDeposit;

}
