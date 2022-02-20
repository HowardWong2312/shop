package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("提现表单")
public class UserWithdrawForm extends PaymentForm {

    @ApiModelProperty(value = "银行卡ID")
    private Long userBankId;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

}
