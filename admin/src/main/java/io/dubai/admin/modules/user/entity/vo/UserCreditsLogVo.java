package io.dubai.admin.modules.user.entity.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.dubai.admin.modules.user.entity.UserBalanceLog;
import io.dubai.admin.modules.user.entity.UserCreditsLog;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 资金记录
 * @date 2021-12-14 18:27:22
 */
@Data
public class UserCreditsLogVo extends UserCreditsLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String statusValue;

    private String statusColor;

}