package io.dubai.common.enums;

import lombok.AllArgsConstructor;

/**
 * 积分明细类型
 */
@AllArgsConstructor
public enum UserCreditsLogStatusEnum {

    INVITED_USER(1, "邀请新用户"),
    SIGNED(2, "打卡签到"),
    CREATED_GROUP(3, "建群奖励"),
    SENT_VIDEO(4, "发视频"),
    CANCELED_GOODS_GROUP_AD(5, "撤销拼团活动"),
    RELEASE_GOODS_GROUP_AD(6, "发布拼团活动"),
    IMPROVE_USER_LEVEL(7, "会员等级提升"),
    COMMISSION(8, "推广分成"),
    GROUP_AWARD(9, "拼团积分奖励"),
    LOTTERY(10, "免费抽奖"),
    CREDITS_EXCHANGE(11, "积分兑现"),
    MANUAL_RECHARGE(12, "人工充值"),
    MANUAL_DEDUCTION(13, "人工扣除"),
    NEW_USER(14, "新用户注册"),
    OTHER(0, "其它");


    public final Integer code;
    public final String desc;

}
