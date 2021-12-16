package io.dubai.admin.modules.goods.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopGoodsCategoryFrom implements Serializable {

    private Long parentId;

    private Long languageId;

    private Integer orderNum;

    private String defaultTitle;

    private String languageTitle;

    private String defaultIconUrl;

    private String languageIconUrl;

}
