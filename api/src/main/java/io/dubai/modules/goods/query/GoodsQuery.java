package io.dubai.modules.goods.query;

import io.dubai.common.utils.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商品查询参数")
public class GoodsQuery extends Query {

    @ApiModelProperty("国家ID")
    private Long countryId;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    private List<Long> categoryIds;

    @ApiModelProperty("是否拼团(1是，0否)")
    private Integer isGroup;

    @ApiModelProperty("是否免费抢(1是，0否)")
    private Integer isRush;

    @ApiModelProperty("是否一元购(1是，0否)")
    private Integer isOneBuy;

    @ApiModelProperty("语言ID")
    private String languageId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("关键字")
    private String key;

}
