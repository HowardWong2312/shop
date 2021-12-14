package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("设置支付密码")
public class PaymentPasswordForm {

    @ApiModelProperty("密码")
    private String password;

}
