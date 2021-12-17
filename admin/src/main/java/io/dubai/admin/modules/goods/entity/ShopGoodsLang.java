package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 商品标题
 * @date 2021-12-17 14:05:29
 */
@Data
@TableName("shop_goods_lang")
public class ShopGoodsLang implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //商品ID
    private Long goodsId;

    //商品名称
    private String title;

    //logo
    private String logoUrl;

    //描述
    private String description;

    //语言ID
    private Long languageId;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
