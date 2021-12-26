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
 * @name 用户等级
 * @date 2021-12-14 18:27:22
 */
@Data
@TableName("t_user_level")
public class UserLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Integer id;

    //需要积分
    @TableField("needsCredits")
    private BigDecimal needsCredits;

    //需要邀请人数
    @TableField("needsInviteNum")
    private Integer needsInviteNum;

    //无条件升级价格
    private BigDecimal price;

    //每天可砍单次数
    @TableField("groupNum")
    private Integer groupNum;

    //累计可砍单次数
    @TableField("totalGroupNum")
    private Integer totalGroupNum;

    //累计可签到获积分的次数
    @TableField("totalSignNum")
    private Integer totalSignNum;

    //累计可发视频获积分的次数
    @TableField("totalVideoNum")
    private Integer totalVideoNum;

    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
