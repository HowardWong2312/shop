package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 国家表
 * @date 2021-12-19 14:07:37
 */
@Data
@TableName("t_country")
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Integer id;

    //名称
    private String name;

    //区号
    private String code;


}
