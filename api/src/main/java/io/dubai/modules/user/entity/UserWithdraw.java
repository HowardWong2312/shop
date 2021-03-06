package io.dubai.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@Data
@TableName("t_user_withdraw")
@ApiModel(value = "用户提现")
public class UserWithdraw implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    @TableId
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "收款方式ID")
    private Long paymentId;

    @ApiModelProperty(value = "收款方式名称")
    @TableField(exist = false)
    private String paymentName;

    @ApiModelProperty(value = "账户名称")
    private String accountName;

    @ApiModelProperty(value = "账户号码")
    private String accountNumber;

    @ApiModelProperty(value = "备用字段")
    private String temp;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

    //实际打款金额
    private BigDecimal realAmount;

    //手续费
    private BigDecimal fee;

    //税
    private BigDecimal tax;

    //佣金扣除
    private BigDecimal rebate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态，0:等待审核,1:审核成功,2:审核失败")
    private Integer status;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.UPDATE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updateTime;


}
