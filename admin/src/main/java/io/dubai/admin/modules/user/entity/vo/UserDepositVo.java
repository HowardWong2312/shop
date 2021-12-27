package io.dubai.admin.modules.user.entity.vo;

import io.dubai.admin.modules.user.entity.UserDeposit;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 充值记录
 * @date 2021-12-14 18:27:22
 */
@Data
public class UserDepositVo extends UserDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String paymentName;

    private String statusValue;

    private String statusColor;

}
