package io.dubai.admin.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author mother fucker
 * @name 积分明细
 * @date 2021-12-25 14:12:53
 */
@Data
@TableName("t_credits_his")
public class TCreditsHis implements Serializable {
	private static final long serialVersionUID = 1L;

	//
		@TableId
								private Integer id;

	//
								private Integer userid;

	//
								private Integer itemid;

	//1.邀请奖励+2.任务奖励+3.新用户注册+4.打赏收入+5.打赏支出-6.兑换 商品消耗-7.取消订单+
								private Integer type;

	//
								private BigDecimal credits;

	//
								private BigDecimal aftercredits;

	//时间
			@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
							private LocalDateTime createtime;

	//描述
								private String descr;

	//是否删除0否1是
								private Integer del;


}
