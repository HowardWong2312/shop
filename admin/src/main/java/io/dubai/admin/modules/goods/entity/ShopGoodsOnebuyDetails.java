package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 一元购记录
 * @date 2022-01-03 17:49:28
 */
@Data
@TableName("shop_goods_onebuy_details")
public class ShopGoodsOnebuyDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //用户ID
    private Long userId;

    //商品id
    private Long goodsId;

    //金额
    private BigDecimal amount;

    //份数
    private Integer quantity;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
