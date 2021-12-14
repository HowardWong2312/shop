package io.dubai.admin.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 提现申请表
 * @date 2021-12-14 18:27:22
 */
@Data
@TableName("t_user_bank")
public class UserBank implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //用户id
    private Long userId;

    //
    private String accountName;

    //银行名称
    private String bankName;

    //银行卡号
    private String accountNumber;

    //支行名称
    private String branchName;

    //
    private String iban;

    //
    private String ifsc;

    //
    private String upi;

    //
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;

    //
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
