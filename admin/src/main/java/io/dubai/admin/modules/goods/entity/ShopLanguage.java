package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 语言
 * @date 2021-12-15 20:01:22
 */
@Data
@TableName("shop_language")
public class ShopLanguage implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //语言标题
    private String title;

    //语言名称
    private String name;

    //语言代码
    private String lang;


}
