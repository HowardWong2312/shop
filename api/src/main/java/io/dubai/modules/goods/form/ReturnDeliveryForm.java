package io.dubai.modules.goods.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单退货")
public class ReturnDeliveryForm {

    @ApiModelProperty("订单号")
    private String orderCode;

    @ApiModelProperty("退货理由")
    private String returnReason;

}
