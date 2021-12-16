package io.dubai.modules.goods.entity.vo;

import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.GoodsCategory;
import io.dubai.modules.goods.entity.GoodsImg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "商品Vo")
public class GoodsCategoryVo extends GoodsCategory implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "多语言标题")
    private String languageTitle;

    @ApiModelProperty(value = "多语言icon")
    private String languageIconUrl;

    @ApiModelProperty(value = "多语言icon")
    private List<GoodsCategoryVo> kids;

}
