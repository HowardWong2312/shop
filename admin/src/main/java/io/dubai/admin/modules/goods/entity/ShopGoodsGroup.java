package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author mother fucker
 * @name 拼团商品
 * @date 2021-12-19 20:21:24
 */
@Data
@TableName("shop_goods_group")
public class ShopGoodsGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //商品id
    private Long goodsId;

    //拼团价
    private BigDecimal price;

    //每个团人数
    private Integer userNum;

    //每份奖励
    private BigDecimal award;

    //剩余份数
    private Integer awardNum;

    //拼团有效期（小时）
    private Integer period;

    //状态(0,已结束,1:开始中)
    private Integer status;

    //
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
