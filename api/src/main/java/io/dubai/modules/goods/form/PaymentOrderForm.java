package io.dubai.modules.goods.form;

import io.dubai.modules.user.form.PaymentForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单支付")
public class PaymentOrderForm extends PaymentForm {

    @ApiModelProperty("订单号")
    private String orderCode;

    @ApiModelProperty("收货地址ID")
    private Long userAddressId;


}
