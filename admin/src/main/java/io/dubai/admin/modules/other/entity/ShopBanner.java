package io.dubai.admin.modules.other.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 轮播图
 * @date 2022-01-04 19:01:44
 */
@Data
@TableName("shop_banner")
public class ShopBanner implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //图片
    private String imgUrl;

    //链接类型(0空,1商品,2资讯,3外链)
    private Integer linkType;

    //链接
    private String linkUrl;

    //排序
    private Integer orderNum;

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
