package io.dubai.admin.modules.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 用户信息表
 * @date 2021-12-14 18:27:22
 */
@Data
@TableName("t_user_info")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Integer userid;

    //国家编码
    private String countrycode;

    //手机号
    private String phone;

    //昵称
    private String nickname;

    //头像
    private String header;

    //性别 男/女
    private String sex;

    //生日
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;

    //年龄
    private Integer age;

    //bibi号（8位纯数字）
    private String bibicode;

    //邀请码
    private String invitecode;

    //邀请人
    private Integer fatherid;

    //当前积分
    private BigDecimal credits;

    //累计收益积分
    private BigDecimal incomecredits;

    //环信id
    private String easemobid;

    //环信密码
    private String easemobpwd;

    //个性签名
    private String signature;

    //肤色
    private String skincolor;

    //财富值
    private BigDecimal wealth;

    //财富等级
    private Integer wealthlevel;

    //背景图
    private String backimage;

    //国家id
    private Integer countryid;

    //国家
    private String country;

    //省id
    private Integer provinceid;

    //省级名称
    private String provincename;

    //市id
    private Integer cityid;

    //市级名称
    private String cityname;

    //二维码
    private String ecode;

    //手机号修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime phonechangetime;

    //bibi号修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime bibicodechangetime;

    //添加好友是否需要验证0否1是
    private Integer verifyfriend;

    //是否他可以通过手机号添加好友
    private Integer isphoneaddfriend;

    //是否可以通过bibi号添加好友
    private Integer isbibicodeaddfriend;

    //是否可以通过群聊添加好友
    private Integer isgroupaddfriend;

    //是否可以通过二维码添加好友
    private Integer isecodeaddfriend;

    //0.未认证1.已认证2黑名单
    private Integer isapprove;

    //语言（默认中文）
    private String language;

    //是否弃用 0否 1是
    private Integer del;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createtime;

    //是否可以创建群0否1是
    private Integer iscancreategroup;

    //手机号搜索（国家编码+手机号）
    private String searchphone;

    //是否商家
    private Integer ismerchant;

    //余额
    private BigDecimal balance;

    //支付密码
    private String password;

    //等级(id就等于等级数)
    private Integer userlevelid;

    //免费抽奖次数
    private Integer lotterytimes;


}
