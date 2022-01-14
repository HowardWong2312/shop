package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.CountryDao;
import io.dubai.admin.modules.goods.entity.Country;
import io.dubai.admin.modules.goods.entity.TCity;
import io.dubai.admin.modules.goods.entity.TProvince;
import io.dubai.admin.modules.goods.entity.vo.CityVo;
import io.dubai.admin.modules.goods.entity.vo.CityVoToRedisData;
import io.dubai.admin.modules.goods.service.CountryService;
import io.dubai.admin.modules.goods.service.TCityService;
import io.dubai.admin.modules.goods.service.TProvinceService;
import io.dubai.common.utils.RedisKeys;
import io.dubai.common.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("CountryService")
public class CountryServiceImpl extends ServiceImpl<CountryDao, Country> implements CountryService {

    @Resource
    private TCityService tCityService;

    @Resource
    private TProvinceService tProvinceService;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public List<CityVo> queryPage(Map<String, Object> params) {
        CityVoToRedisData cityVoToRedisData = redisUtils.get(RedisKeys.citiesKey, CityVoToRedisData.class);
        if (cityVoToRedisData != null) {
            return cityVoToRedisData.getList();
        }
        List<Country> country = this.list();
        List<CityVo> cityVos = new ArrayList<>();
        for (Country countryItem : country) {
            cityVos.add(new CityVo(countryItem.getId(), 0, countryItem.getName(), 0));
            List<TProvince> provinces = tProvinceService.getBaseMapper().selectList(new QueryWrapper<TProvince>().eq("countryId", countryItem.getId()));
            for (TProvince provinceItem : provinces) {
                cityVos.add(new CityVo(provinceItem.getId(), provinceItem.getCountryid(), provinceItem.getName(), 1));
                List<TCity> cities = tCityService.getBaseMapper().selectList(new QueryWrapper<TCity>().eq("provinceId", provinceItem.getId()));
                for (TCity city : cities) {
                    cityVos.add(new CityVo(city.getId(), city.getProvinceid(), city.getName(), 2));
                }
            }
        }

        redisUtils.set(RedisKeys.citiesKey, new CityVoToRedisData(cityVos), -1);
        return cityVos;
    }

    /**
     * @param id
     * @param type
     * @param action 0.新增 1.修改 2.删除
     */
    public void reload(Integer id, Integer type, Integer action, CityVo cityVo) {
        CityVoToRedisData cityVoToRedisData = redisUtils.get(RedisKeys.citiesKey, CityVoToRedisData.class);
        if (cityVoToRedisData == null) {
            return;
        }
        if (action.equals(0)) {
            cityVoToRedisData.getList().add(cityVo);
            redisUtils.set(RedisKeys.citiesKey, cityVoToRedisData, -1);
            return;
        }

        for (int i = 0; i < cityVoToRedisData.getList().size(); i++) {
            if (cityVoToRedisData.getList().get(i).getId().equals(id) && cityVoToRedisData.getList().get(i).getType().equals(type)) {
                switch (action) {
                    case 1:
                        cityVoToRedisData.getList().get(i).setName(cityVo.getName());
                        break;
                    case 2:
                        cityVoToRedisData.getList().remove(i);
                        break;
                }
            }
        }


        redisUtils.set(RedisKeys.citiesKey, cityVoToRedisData, -1);
    }
}

