package io.dubai.common.enums;

import lombok.AllArgsConstructor;

/**
 * API接口统一返回状态码
 */
@AllArgsConstructor
public enum ResponseStatusEnum {

    SUCCESS(200, "SUCCESS"),
    SYSTEM_ERROR(500, "something went wrong"),
    PAYMENT_UNAVAILABLE(701, "此支付方式不可用，请稍后再试"),
    PLZ_CHOOSE_IMAGE(801, "请选择一张图片"),
    NOT_LOGO_IMG(901, "请上传LOGO"),
    NOT_BANNER_IMG(902, "请上传至少一个banner图"),
    NOT_DESC_IMG(903, "请上传至少一个详情图"),
    LOGO_IMG_FORMAT_ERROR(904, "logo图片上传失败"),
    NOT_AUTH_TOKEN(1001, "缺少token"),
    AUTH_TOKEN_EXPIRED(1002, "token已过期"),
    PAY_PASSWORD_NOT_EXIST(1003, "支付密码未设置"),
    PAY_PASSWORD_ERROR(1004, "支付密码错误"),
    BALANCE_INSUFFICIENT(1005, "余额不足"),
    GROUP_RECORD_NOT_EXIST(2001, "当前拼团不存在"),
    GROUP_RECORD_IS_DONE(2002, "当前拼团已满，请尝试参与其它团或自行发起拼团"),
    GROUP_NOT_EXIST(2003, "当前商品不支持拼团"),
    ORDER_HAS_BEEN_UPDATED(2004, "当前订单已被处理，无法处理"),
    USER_HAS_BEEN_JOINED_GROUP(2005, "当前用户已参过此团"),
    ORDER_NOT_YET_PAY(2006, "订单未支付"),
    ORDER_EXPIRED(2007, "订单已过期"),
    ALREADY_TAKE_CASH_AWARD(2008, "已领取过返现"),
    THERE_IS_NOT_CASH_AWARD(2009, "此商品没有返现奖励"),
    THERE_IS_NOT_MORE_CASH_AWARD(2010, "此商品返现奖励已被领取完了"),
    GOODS_STOCK_NOT_ENOUGH(2011, "库存不足"),
    GROUP_ORDER_NOT_YET_CONFIRM(2012, "拼团还未成团"),
    CREDITS_INSUFFICIENT(2013, "积分不足"),
    ORDER_NOT_YET_DELIVERY(2014, "该订单还未发货"),
    ORDER_NOT_YET_CONFIRM_TO_DELIVERY(2015, "该订单尚未确认需发货"),
    PAYMENT_PASSWORD_EXISTED(2016, "已经设置过支付密码"),
    ALREADY_SIGNED(3001, "今日已打卡，请明日再试"),
    NO_MORE_HIGHER_LEVEL(3002, "当前会员等级已是最高级别"),
    CANNOT_IMPROVE_USER_LEVEL(3003, "升级条件没达到"),
    INVITE_CODE_NOT_EXIST(3004, "邀请码不存在"),
    USER_JOINED_TEAM(3005, "该用户已加入了一个团队"),
    USER_NOT_JOIN_TEAM_YET(3006, "该用户还没有加入团队"),
    CANNOT_SIGN_MORE(3007, "已达到该等级的签到次数上限"),
    ONLY_CAN_BUY_ONE(3008, "一元购商品限购一份"),
    USER_DONT_BELONG_THE_TEAM(3009, "该用户不属于此商家的团队，无法参与一元购活动"),
    ONE_BUY_EVENT_OVER(3010, "该商品一元购活动已经结束"),
    LOTTERY_TIMES_RAN_OUT(3011, "抽奖次数已用完"),
    RUSH_EVENT_OVER(3012, "该商品抽奖活动已结束"),
    EXCHANGE_CLOSED(4001, "积分兑现暂时关闭中"),
    MUST_BE_MORE_THAN_TWENTY(4002, "最少兑换20积分"),
    NEEDS_BIND_BANK(4003, "请先绑定至少一张银行卡"),
    CANNOT_EXCHANGE(4004, "该用户不符合积分兑现的资格"),
    CREDITS_SOLD(5001, "购买失败，该积分已卖出"),
    PARAMS_IS_ERROR(502, "params error");


    public final Integer code;
    public final String msg;

}
