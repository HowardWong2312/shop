package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("购买积分")
public class CancelSellCreditsForm {

    @ApiModelProperty(value = "交易ID")
    private Long userCreditsExchangeId;

}
