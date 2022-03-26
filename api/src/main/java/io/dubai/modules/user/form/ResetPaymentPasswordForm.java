package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("重置支付密码")
public class ResetPaymentPasswordForm {

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "支付密码")
    private String password;

}
