package io.dubai.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-11 20:02:04
 */
@Data
@TableName("shop_goods_lang")
@ApiModel(value = "商品标题")
public class GoodsLang implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "")
    @TableId
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "logo")
    private String logoUrl;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "语言ID")
    private Long languageId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updateTime;


}
