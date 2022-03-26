package io.dubai.admin.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 提现申请表
 * @date 2021-12-14 18:27:22
 */
@Data
@TableName("t_user_deposit")
public class UserDeposit implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private String orderCode;

    //用户id
    private Long userId;

    //支付方式id
    private Long paymentId;

    //提现金额
    private BigDecimal amount;

    //0:待处理,1:充值成功,2:超时
    @TableField("`status`")
    private Integer status;

    //
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;

    //
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
