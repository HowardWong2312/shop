package io.dubai.admin.modules.goods.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopGoodsCategoryVo {

    private Long id;

    private Long parentId;

    private String defaultTitle;

    private String defaultIconUrl;

    private String languageTitle;

    private String languageIconUrl;

    private String name;

    private Integer isDel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
