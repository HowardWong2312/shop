package io.dubai.modules.other.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@Data
public class Msg implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    @TableId
    private Long userId;

    @ApiModelProperty(value = "图片")
    private String title;

    @ApiModelProperty(value = "链接")
    private String id;




}
