package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("充值表单")
public class UserDepositForm extends PaymentForm {

    @ApiModelProperty(value = "充值金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "充值方式ID")
    private Long paymentId;

}
