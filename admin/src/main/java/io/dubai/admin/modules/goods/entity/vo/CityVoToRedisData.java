package io.dubai.admin.modules.goods.entity.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CityVoToRedisData implements Serializable {
    private static final long serialVersionUID = 1L;

    private CityVoToRedisData() {

    }

    public CityVoToRedisData(List<CityVo> list) {
        this.list = list;
    }

    private List<CityVo> list;
}
