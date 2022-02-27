package io.dubai.modules.other.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dubai.modules.other.entity.Payment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@Data
public class PaymentVo extends Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "多语言标题")
    private String languageTitle;

    @ApiModelProperty(value = "多语言描述")
    private String languageDescription;


}
