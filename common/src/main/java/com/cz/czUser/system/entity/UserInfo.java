package com.cz.czUser.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * TODO 在此加入类描述
 * @copyright {@link 9iu.org}
 * @author springrain<Auto generate>
 * @version  2021-06-29 17:50:08
 */
@Data
@TableName("t_user_info")
public class UserInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * userId
	 */
	@TableId(value = "userId",type = IdType.INPUT)
	private Integer userId;
	/**
	 * 国家编码
	 */
	@TableField("countryCode")
	private String countryCode;
	/**
	 * 手机号
	 */
	@TableField("phone")
	private String phone;
	/**
	 * 昵称
	 */
	@TableField("nickName")
	private String nickName;
	/**
	 * 头像
	 */
	@TableField("header")
	private String header;
	/**
	 * 性别 男/女
	 */
	@TableField("sex")
	private String sex;
	/**
	 * 生日
	 */
	@TableField("birthday")
	private Timestamp birthday;
	/**
	 * 年龄
	 */
	@TableField("age")
	private Integer age;
	/**
	 * bibi号（8位纯数字）
	 */
	@TableField("bibiCode")
	private String bibiCode;
	/**
	 * 邀请码
	 */
	@TableField("inviteCode")
	private String inviteCode;
	/**
	 * 邀请人
	 */
	@TableField("fatherId")
	private Integer fatherId;
	/**
	 * 当前积分
	 */
	@TableField("credits")
	private BigDecimal credits;
	/**
	 * 累计收益积分
	 */
	@TableField("incomeCredits")
	private BigDecimal incomeCredits;
	/**
	 * 环信id
	 */
	@TableField("easemobId")
	private String easemobId;
	/**
	 * 环信密码
	 */
	@TableField("easemobPwd")
	private String easemobPwd;
	/**
	 * 个性签名
	 */
	@TableField("signature")
	private String signature;
	/**
	 * 肤色
	 */
	@TableField("skinColor")
	private String skinColor;
	/**
	 * 财富值
	 */
	@TableField("wealth")
	private BigDecimal wealth;
	/**
	 * 财富等级
	 */
	@TableField("wealthLevel")
	private Integer wealthLevel;
	/**
	 * 背景图
	 */
	@TableField("backImage")
	private String backImage;
	/**
	 * 国家id
	 */
	@TableField("countryId")
	private Integer countryId;
	/**
	 * 国家
	 */
	@TableField("country")
	private String country;
	/**
	 * 省id
	 */
	@TableField("provinceId")
	private Integer provinceId;
	/**
	 * 省级名称
	 */
	@TableField("provinceName")
	private String provinceName;
	/**
	 * 市id
	 */
	@TableField("cityId")
	private Integer cityId;
	/**
	 * 市级名称
	 */
	@TableField("cityName")
	private String cityName;
	/**
	 * 二维码
	 */
	@TableField("eCode")
	private String eCode;
	/**
	 * 手机号修改时间
	 */
	@TableField("phoneChangeTime")
	private Date phoneChangeTime;
	/**
	 * bibi号修改时间
	 */
	@TableField("bibiCodeChangeTime")
	private Date bibiCodeChangeTime;
	/**
	 * 添加好友是否需要验证0否1是
	 */
	@TableField("verifyFriend")
	private Integer verifyFriend;
	/**
	 * 是否他可以通过手机号添加好友
	 */
	@TableField("isPhoneAddFriend")
	private Integer isPhoneAddFriend;
	/**
	 * 是否可以通过bibi号添加好友
	 */
	@TableField("isBiBiCodeAddFriend")
	private Integer isBiBiCodeAddFriend;
	/**
	 * 是否可以通过群聊添加好友
	 */
	@TableField("isGroupAddFriend")
	private Integer isGroupAddFriend;
	/**
	 * 是否可以通过二维码添加好友
	 */
	@TableField("isECodeAddFriend")
	private Integer isECodeAddFriend;
	/**
	 * 是否认证0否1是
	 */
	@TableField("isApprove")
	private Integer isApprove;
	/**
	 * 语言（默认中文）
	 */
	@TableField("language")
	private String language;
	/**
	 * 是否弃用 0否 1是
	 */
	@TableField("del")
	private Integer del;
	/**
	 * createTime
	 */
	@TableField("createTime")
	private Timestamp createTime;
	/**
	 * 是否可以创建群0否1是
	 */
	@TableField("isCanCreateGroup")
	private Integer isCanCreateGroup;
	/**
	 * 手机号搜索（国家编码+手机号）
	 */
	@TableField("searchPhone")
	private String searchPhone;


	/**
	 * 地址
	 */
	@TableField(exist = false)
	private String address;

	@TableField(exist = false)
	private String token;

	@TableField(exist = false)
	private Integer isAnnoy;//是否免打扰0否1是

	@TableField(exist = false)
	private Integer isFriend;//是否好友0否1是

	@TableField(exist = false)
	private Integer isBlack;//是否黑名单0否1是

	@TableField(exist = false)
	private Integer isBlocked;//是否被拉黑0否1是

	@TableField(exist = false)
	private Integer friendStatus;//添加好友状态：1.等待对方同意，2.等待你同意3.已添加好友4.未添加5.已过期6.别人将你删除7.你将别人删除

	@TableField(exist = false)
	private Integer haveNum; //邀请人数

	@TableField(exist = false)
	private Integer needNum; //下一届阶段需要邀请人数

	@TableField(exist = false)
	private Integer isHavePwd;//是否设置密码0否1是

	@TableField(exist = false)
	private String fatherName;//邀请人姓名

	@TableField("isMerchant")
	private Integer isMerchant; // 是否为电商商家

	private BigDecimal balance;//账户余额

	private String password;//支付密码

	@TableField("userLevelId")
	private Long userLevelId;//用户等级

	@TableField("lotteryTimes")
	private Integer lotteryTimes;//免费抽奖次数

	@TableField("isLockCredits")
	private Integer isLockCredits;//是否锁定积分

}

	
