package io.dubai.modules.goods.form;

import io.dubai.modules.goods.entity.GoodsImg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("增加商品语言")
public class GoodsLanguageForm {

    @ApiModelProperty("商品语言ID，修改时作为主键")
    private Long goodsLangId;

    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("语言ID")
    private Long languageId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("logo")
    private String logoUrl;

    @ApiModelProperty("轮播图")
    private List<GoodsImg> bannerList;

    @ApiModelProperty("详情图")
    private List<GoodsImg> descImgList;

}
