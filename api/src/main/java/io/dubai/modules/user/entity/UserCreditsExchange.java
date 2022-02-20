package io.dubai.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 积分交易
 * @date 2022-01-12 19:15:13
 */
@Data
@TableName("t_user_credits_exchange")
public class UserCreditsExchange implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //卖家
    private Long userId;

    //数量
    private BigDecimal quantity;

    //单价
    private BigDecimal price;

    //买家
    private Long buyerId;

    //0上架中，1已卖出，2已下架
    private Integer status;

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
