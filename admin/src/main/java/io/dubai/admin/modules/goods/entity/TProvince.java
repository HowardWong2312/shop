package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 省级表
 * @date 2022-01-14 18:51:41
 */
@Data
@TableName("t_province")
public class TProvince implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Integer id;

    //名称
    private String name;

    //国家id
    private Integer countryid;


    @TableField(exist = false)
    private Integer type;


    @TableField(exist = false)
    private Integer parentId;
}
