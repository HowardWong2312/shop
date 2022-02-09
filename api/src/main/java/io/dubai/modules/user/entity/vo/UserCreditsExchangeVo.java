package io.dubai.modules.user.entity.vo;

import io.dubai.modules.user.entity.UserCreditsExchange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "积分交易vo")
public class UserCreditsExchangeVo extends UserCreditsExchange implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "卖家名称")
    private String seller;

    @ApiModelProperty(value = "买家名称")
    private String buyer;


}
