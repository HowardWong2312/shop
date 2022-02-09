package io.dubai.admin.modules.user.entity.vo;

import io.dubai.admin.modules.user.entity.UserAddress;
import io.dubai.admin.modules.user.entity.UserBank;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 资金记录
 * @date 2021-12-14 18:27:22
 */
@Data
public class UserAddressVo extends UserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sysUserName;

    private String sysDeptName;

    private String userName;

}
