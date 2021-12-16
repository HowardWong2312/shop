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
@TableName("t_user_level")
@ApiModel(value = "用户等级")
public class UserLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "等级")
    @TableId(type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "需要积分")
    @TableField("needsCredits")
    private BigDecimal needsCredits;

    @ApiModelProperty(value = "需要邀请人数")
    @TableField("needsInviteNum")
    private Integer needsInviteNum;

    @ApiModelProperty(value = "无条件升级价格")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty(value = "每天可砍单数")
    @TableField("groupNum")
    private Integer groupNum;

    @ApiModelProperty(value = "累计可砍单数")
    @TableField("totalGroupNum")
    private Integer totalGroupNum;

    @ApiModelProperty(value = "累计可签到获积分的次数")
    @TableField("totalSignNum")
    private Integer totalSignNum;

    @ApiModelProperty(value = "累计可发视频获积分的次数")
    @TableField("totalVideoNum")
    private Integer totalVideoNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT,value = "createTime")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updateTime;


}
