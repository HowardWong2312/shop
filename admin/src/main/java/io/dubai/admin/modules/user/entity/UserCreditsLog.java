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
@TableName("t_user_credits_log")
public class UserCreditsLog implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //账户ID
    @TableField("userId")
    private Long userId;

    //变动积分
    private BigDecimal amount;

    //变后积分余额
    private BigDecimal balance;

    //
    private Integer status;

    //1收入，2支出
    private Integer type;

    //
    private String desc;

    @TableField(value = "createTime",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(value = "updateTime",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;


}
