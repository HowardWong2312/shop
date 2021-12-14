package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("提现表单")
public class UserWithdrawForm {

    @ApiModelProperty(value = "银行卡ID")
    private Long userBankId;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付密码")
    private String password;

}
