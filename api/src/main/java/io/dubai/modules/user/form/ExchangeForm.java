package io.dubai.modules.user.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("积分兑现表单")
public class ExchangeForm {

    @ApiModelProperty(value = "积分")
    private BigDecimal credits;

}
