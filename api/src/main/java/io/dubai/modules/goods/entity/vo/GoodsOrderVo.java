package io.dubai.modules.goods.entity.vo;

import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.GoodsGroup;
import io.dubai.modules.goods.entity.GoodsImg;
import io.dubai.modules.goods.entity.GoodsOrder;
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
@ApiModel(value = "商品订单Vo")
public class GoodsOrderVo extends GoodsOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "默认标题")
    private String title;

    @ApiModelProperty(value = "默认logo")
    private String logoUrl;

    @ApiModelProperty(value = "多语言标题")
    private String languageTitle;

    @ApiModelProperty(value = "多语言logo")
    private String languageLogoUrl;

    @ApiModelProperty(value = "商家ID")
    private Long merchantId;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "商家头像")
    private String merchantFaceImg;

    @ApiModelProperty(value = "买家名称")
    private String userName;

    @ApiModelProperty(value = "买家头像")
    private String userFaceImg;

    @ApiModelProperty(value = "参团列表")
    private List<GoodsGroupRecordDetailsVo> goodsGroupRecordDetailsList;

    @ApiModelProperty(value = "拼团记录")
    private GoodsGroupRecordVo goodsGroupRecord;

    @ApiModelProperty(value = "拼团订单支付截止时间戳")
    private Long payPeriodTime;



}
