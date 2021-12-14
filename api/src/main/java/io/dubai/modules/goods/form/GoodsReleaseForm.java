package io.dubai.modules.goods.form;

import io.dubai.modules.goods.entity.GoodsGroup;
import io.dubai.modules.goods.entity.GoodsImg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("发布商品")
public class GoodsReleaseForm {

    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("语言ID")
    private Long languageId;

    @ApiModelProperty("国家ID")
    private Long countryId = 0L;

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

    @ApiModelProperty("库存")
    private Integer stock = 0;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "外链")
    private String linkUrl;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "状态")
    private Integer status = 1;

}
