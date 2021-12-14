package io.dubai.modules.goods.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("设置拼团活动")
public class SetGoodsGroupForm {

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "拼团价")
    private BigDecimal price;

    @ApiModelProperty(value = "每个团人数")
    private Integer userNum;

    @ApiModelProperty(value = "每份奖励")
    private BigDecimal award;

    @ApiModelProperty(value = "奖励份数")
    private Integer awardNum;

    @ApiModelProperty(value = "拼团有效期（小时）")
    private Integer period;


}
