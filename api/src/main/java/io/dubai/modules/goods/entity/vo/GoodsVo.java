package io.dubai.modules.goods.entity.vo;

import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.GoodsGroup;
import io.dubai.modules.goods.entity.GoodsImg;
import io.dubai.modules.goods.entity.GoodsOneBuy;
import io.dubai.modules.other.entity.Language;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "商品Vo")
public class GoodsVo extends Goods implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "多语言标题")
    private String languageTitle;

    @ApiModelProperty(value = "多语言logo")
    private String languageLogoUrl;

    @ApiModelProperty(value = "多语言描述")
    private String languageDescription;

    @ApiModelProperty(value = "商家是否认证")
    private String isMerchant;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "商家头像")
    private String merchantFaceImg;

    @ApiModelProperty(value = "商家bibi号")
    private String merchantBibiCode;

    @ApiModelProperty(value = "详情图")
    private List<GoodsImg> descImgList;

    @ApiModelProperty(value = "轮播图")
    private List<GoodsImg> bannerList;

    @ApiModelProperty(value = "拼团详情")
    private GoodsGroup goodsGroup;

    @ApiModelProperty(value = "拼团价格")
    private BigDecimal goodsGroupPrice;

    @ApiModelProperty(value = "一元购详情")
    private GoodsOneBuy goodsOneBuy;

    @ApiModelProperty(value = "拼团列表")
    private List<GoodsGroupRecordVo> goodsGroupRecordList;

    @ApiModelProperty(value = "语言对象")
    private Language language;

    @ApiModelProperty(value = "分类对象")
    private GoodsCategoryVo category;

    @ApiModelProperty(value = "是否已收藏")
    private Integer isFavorite = 0;


}
