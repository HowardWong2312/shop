package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 免费抢活动
 * @date 2021-12-16 18:16:04
 */
@Data
@TableName("shop_goods_rush")
public class ShopGoodsRush implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //商品id
    private Long goodsId;

    //剩余份数
    private Integer quantity;

    //到期时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;

    //状态(0,待审核,1:审核成功,2:审核失败)
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
