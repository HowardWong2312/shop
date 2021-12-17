package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 商品分类
 * @date 2021-12-15 15:03:44
 */
@Data
@TableName("shop_goods_category_lang")
public class ShopGoodsCategoryLang implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //分类ID
    private Long goodsCategoryId;

    //分类名称
    private String title;

    //
    private String iconUrl;

    //
    private Long languageId;

    //
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;

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
