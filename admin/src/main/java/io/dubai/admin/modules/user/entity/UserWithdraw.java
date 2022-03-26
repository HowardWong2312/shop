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
@TableName("t_user_withdraw")
public class UserWithdraw implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //用户id
    private Long userId;

    //收款方式
    private Long paymentId;

    //
    private String accountName;

    //
    private String accountNumber;

    //
    private String temp;

    //提现金额
    private BigDecimal amount;

    //实际打款金额
    private BigDecimal realAmount;

    //手续费
    private BigDecimal fee;

    //税
    private BigDecimal tax;

    //佣金扣除
    private BigDecimal rebate;

    //拒绝原因
    private String remark;

    //0:等待审核,1:审核成功,2:审核失败
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
