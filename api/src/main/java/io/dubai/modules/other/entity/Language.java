package io.dubai.modules.other.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-11 20:00:11
 */
@Data
@TableName("shop_language")
@ApiModel(value = "语言")
public class Language implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    @TableId
    private Long id;

    @ApiModelProperty(value = "语言标题")
    private String title;

    @ApiModelProperty(value = "语言名称")
    private String name;

    @ApiModelProperty(value = "语言代码")
    private String lang;


}
