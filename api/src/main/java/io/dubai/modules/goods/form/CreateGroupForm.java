package io.dubai.modules.goods.form;

import io.dubai.modules.user.form.PaymentForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("发起拼团")
public class CreateGroupForm extends PaymentForm {

    @ApiModelProperty("拼团活动ID")
    private Long goodsGroupId;

    @ApiModelProperty("收货地址ID")
    private Long userAddressId;

    @ApiModelProperty("数量")
    private Integer quantity;

}
