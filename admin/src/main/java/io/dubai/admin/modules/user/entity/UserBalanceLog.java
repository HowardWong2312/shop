package io.dubai.admin.modules.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 资金记录
 * @date 2021-12-14 18:27:22
 */
@Data
@TableName("t_user_balance_log")
public class UserBalanceLog implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //账户ID
    private Long userId;

    //交易金额
    private BigDecimal amount;

    //变后余额
    private BigDecimal balance;

    //
    @TableField("`status`")
    private Integer status;

    //1收入，2支出
    @TableField("`type`")
    private Integer type;

    //
    @TableField("`desc`")
    private String desc;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
