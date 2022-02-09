package com.cz.czUser.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_app_user")
public class AppUser {

    @TableId
    private Integer id;

    @TableField("countryCode")
    private String countryCode;

    private String phone;

    @TableField("password")
    private String password;

    @TableField("del")
    private Integer del = 0;


}
