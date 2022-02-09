package io.dubai.modules.goods.form;

import io.dubai.modules.user.form.PaymentForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("购买商品")
public class BuyGoodsForm extends PaymentForm {

    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("收货地址ID")
    private Long userAddressId;

    @ApiModelProperty("数量")
    private Integer quantity;

}
