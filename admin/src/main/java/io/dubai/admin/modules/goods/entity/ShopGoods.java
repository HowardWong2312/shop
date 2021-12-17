package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 商品
 * @date 2021-12-17 14:05:29
 */
@Data
@TableName("shop_goods")
public class ShopGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //国家ID,0为不限
    private Long countryId;

    //用户ID-代表商家
    private Long userId;

    //分类ID
    private Long categoryId;

    //商品名称(默认)
    private String title;

    //备注(默认)
    private String description;

    //产品logo(默认)
    private String logoUrl;

    //库存
    private Integer stock;

    //价格
    private BigDecimal price;

    //外链
    private String linkUrl;

    //联系电话
    private String phone;

    //联系地址
    private String address;

    //是否拼团
    private Integer isGroup;

    //是否免费抢
    private Integer isRush;

    //是否一元购
    private Integer isOneBuy;

    //0:下架，1:上架
    private Integer status;

    //软删除
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;

    //乐观锁
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
