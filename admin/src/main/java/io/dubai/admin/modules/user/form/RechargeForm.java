package io.dubai.admin.modules.user.form;

import io.dubai.admin.modules.user.entity.UserCreditsLog;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * @author mother fucker
 * @name 充值表单
 * @date 2021-12-14 18:27:22
 */
@Data
public class RechargeForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Long> ids;

    private BigDecimal amount;

}
