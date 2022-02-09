package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("支付表单")
public class PaymentForm {

    @ApiModelProperty(value = "支付方式")
    private Long paymentId = 1L;

    @ApiModelProperty(value = "支付密码")
    private String password;

}
