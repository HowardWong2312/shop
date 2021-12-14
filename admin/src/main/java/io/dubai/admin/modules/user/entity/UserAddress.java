package io.dubai.admin.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 收货地址
 * @date 2021-12-14 18:27:22
 */
@Data
@TableName("t_user_address")
public class UserAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Long id;

    //用户ID
    private Long userId;

    //收货人姓名
    private String name;

    //国家代码
    private String countryCode;

    //电话
    private String phone;

    //国家ID
    private Long countryId;

    //省
    private String province;

    //市
    private String city;

    //区
    private String area;

    //详细地址
    private String address;

    //是否默认
    private Integer isDefault;

    //
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;

    //
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
