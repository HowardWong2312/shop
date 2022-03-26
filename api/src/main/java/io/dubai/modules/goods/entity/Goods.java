package io.dubai.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@TableName("shop_goods")
@ApiModel(value = "商品")
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    @TableId
    private Long id;

    @ApiModelProperty(value = "国家ID,0为不限")
    private Long countryId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "商品名称(默认)")
    private String title;

    @ApiModelProperty(value = "备注(默认)")
    private String description;

    @ApiModelProperty(value = "产品logo(默认)")
    private String logoUrl;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "外链")
    private String linkUrl;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "是否拼团")
    private Integer isGroup;

    @ApiModelProperty(value = "是否免费抢")
    private Integer isRush;

    @ApiModelProperty(value = "是否一元购")
    private Integer isOneBuy;

    @ApiModelProperty(value = "0:下架，1：上架")
    private Integer status;

    @ApiModelProperty(value = "软删除")
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableLogic
    private Integer isDel;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Version
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.UPDATE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updateTime;


}
