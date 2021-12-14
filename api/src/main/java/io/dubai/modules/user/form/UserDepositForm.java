package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("充值表单")
public class UserDepositForm {

    @ApiModelProperty(value = "充值方式")
    private Long paymentId;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

}
