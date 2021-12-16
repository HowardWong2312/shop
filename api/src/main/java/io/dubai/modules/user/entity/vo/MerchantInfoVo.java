package io.dubai.modules.user.entity.vo;

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
@ApiModel(value = "商家中心信息")
public class MerchantInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总收入")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "总成交数量")
    private Integer dealCount;

    @ApiModelProperty(value = "已上架商品")
    private Integer goodsCount;

    @ApiModelProperty(value = "总订单数")
    private Integer orderCount;


}
