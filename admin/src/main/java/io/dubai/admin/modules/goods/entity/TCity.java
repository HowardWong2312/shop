package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author mother fucker
 * @name 城市表
 * @date 2022-01-14 18:50:25
 */
@Data
@TableName("t_city")
public class TCity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId
    private Integer id;

    //城市名
    private String name;

    //省id
    private Integer provinceid;

    @TableField(exist = false)
    private Integer type;

    @TableField(exist = false)
    private Integer parentId;


}
