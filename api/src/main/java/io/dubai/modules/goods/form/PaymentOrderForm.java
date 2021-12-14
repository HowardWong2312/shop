package io.dubai.modules.goods.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单支付")
public class PaymentOrderForm {

    @ApiModelProperty("订单号")
    private String orderCode;

    @ApiModelProperty("收货地址ID")
    private Long userAddressId;

    @ApiModelProperty("支付方式ID")
    private Long paymentId;

    @ApiModelProperty("支付密码")
    private String password;

}
