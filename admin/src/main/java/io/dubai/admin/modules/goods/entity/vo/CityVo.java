package io.dubai.admin.modules.goods.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CityVo implements Serializable {
    private static final long serialVersionUID = 1L;

    public CityVo() {
        super();
    }

    public CityVo(Integer id, Integer parentId, String name,Integer type) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
    }

    private Integer id;

    private Integer parentId;

    private String name;

    /*区分类型 0.国家 1.省份 2.城市*/
    private Integer type;
}
