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
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@Data
@TableName("shop_payment")
@ApiModel(value = "支付方式")
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    @TableId
    private Long id;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "是否为充值方式")
    private Integer isDeposit;

    @ApiModelProperty(value = "是否为充值方式")
    private Integer isPayment;

    @ApiModelProperty(value = "软删除")
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableLogic
    private Integer isDel;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Version
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updateTime;


}
