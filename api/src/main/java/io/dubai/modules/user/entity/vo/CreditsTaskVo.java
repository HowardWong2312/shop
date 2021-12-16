package io.dubai.modules.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "积分任务完成情况")
public class CreditsTaskVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "今日是否已签到")
    private Integer isSigned = 0;

    @ApiModelProperty(value = "今日是否已发过视频")
    private Integer isSentVideo = 0;

    @ApiModelProperty(value = "今日是否已建群")
    private Integer isCreatedGroup = 0;

    @ApiModelProperty(value = "今日是否已邀请好友")
    private Integer isInvitedUser = 0;

    @ApiModelProperty(value = "下一个级别")
    private Integer nextLevel = 0;

    @ApiModelProperty(value = "升级所需积分")
    private BigDecimal nextLeveLNeedsCredits = BigDecimal.ZERO;

    @ApiModelProperty(value = "升级所需邀请人数")
    private Integer nextLeveLNeedsInviteUserNum = 0;

    @ApiModelProperty(value = "升级所需要支付")
    private BigDecimal nextLeveLNeedsPay = BigDecimal.ZERO;


}
