package io.dubai.modules.goods.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商家审核退货")
public class CheckReturnDeliveryForm {

    @ApiModelProperty("订单号")
    private String orderCode;

    @ApiModelProperty("审核方向，1：同意，2：拒绝")
    private Integer type;

    @ApiModelProperty("商家备注，拒绝理由")
    private String remark;

}
