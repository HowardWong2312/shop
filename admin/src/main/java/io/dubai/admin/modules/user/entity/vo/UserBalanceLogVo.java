package io.dubai.admin.modules.user.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.dubai.admin.modules.user.entity.UserBalanceLog;
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
public class UserBalanceLogVo extends UserBalanceLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String statusValue;

    private String statusColor;

}
