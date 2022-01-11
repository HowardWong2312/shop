package io.dubai.modules.goods.query;

import io.dubai.common.utils.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商品查询参数")
public class GoodsOrderQuery extends Query {

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("语言ID")
    private String languageId;

    @ApiModelProperty("用户ID")
    private Long userId;

}
