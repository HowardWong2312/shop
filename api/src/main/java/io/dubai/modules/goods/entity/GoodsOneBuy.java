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
@TableName("shop_goods_onebuy")
@ApiModel(value = "一元购商品")
public class GoodsOneBuy implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    @TableId
    private Long id;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "剩余份数")
    private Integer quantity;

    @ApiModelProperty(value = "到期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "状态(0,待审核,1:审核成功,2:审核失败)")
    private Integer status;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Version
    private Integer version;

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
