package io.dubai.modules.goods.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单发货")
public class OrderDeliveryForm {

    @ApiModelProperty("订单号")
    private String orderCode;

    @ApiModelProperty("物流公司")
    private String logistics;

    @ApiModelProperty("物流单号")
    private String logisticsCode;

}
