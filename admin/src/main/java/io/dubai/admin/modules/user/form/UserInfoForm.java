package io.dubai.admin.modules.user.form;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * @author mother fucker
 * @name 充值表单
 * @date 2021-12-14 18:27:22
 */
@Data
public class UserInfoForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String countryCode;

    private String phone;

    private String bibiCode;

    private String loginPassword;

    private String nickName;

    private String header;

    private Integer fatherId;

    private Integer isMerchant;

    private Long userLevelId;

    private Long sysUserId;

    private Integer lotteryTimes;

}
