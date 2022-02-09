package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("购买积分")
public class BuyCreditsForm extends PaymentForm {

    @ApiModelProperty(value = "交易ID")
    private Long userCreditsExchangeId;

}
