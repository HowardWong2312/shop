package io.dubai.admin.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.goods.entity.Country;
import io.dubai.admin.modules.goods.entity.vo.CityVo;
import io.dubai.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 国家表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-19 14:07:37
 */
public interface CountryService extends IService<Country> {

    List<CityVo> queryPage(Map<String, Object> params);


    /**
     * @param id
     * @param type
     * @param action 0.新增 1.修改 2.删除
     */
    void reload(Integer id, Integer type, Integer action, CityVo cityVo);
}

