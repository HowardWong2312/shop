package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.entity.TCity;
import io.dubai.admin.modules.goods.entity.vo.CityVo;
import io.dubai.admin.modules.goods.service.CountryService;
import io.dubai.admin.modules.goods.service.TCityService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 城市表
 *
 * @author mother fucker
 * @name 城市表
 * @date 2022-01-14 18:50:25
 */
@RestController
@RequestMapping("goods/tCity")
public class TCityController {
    @Resource
    private TCityService tCityService;

    @Resource
    private CountryService countryService;


    @GetMapping("/list")
    @RequiresPermissions("goods:tCity:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = tCityService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:tCity:info")
    public R info(@PathVariable("id") Integer id) {
        TCity tCity = tCityService.getById(id);

        return R.ok().put("tCity", tCity);
    }

    @PostMapping("/save")
    @RequiresPermissions("goods:tCity:save")
    public R save(@RequestBody TCity tCity) {
        tCityService.save(tCity);
        countryService.reload(tCity.getId(), tCity.getType(), 0, new CityVo(tCity.getId(), tCity.getParentId(), tCity.getName(), tCity.getType()));
        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:tCity:update")
    public R update(@RequestBody TCity tCity) {
        ValidatorUtils.validateEntity(tCity);
        tCityService.updateById(tCity);
        CityVo cityVo = new CityVo();
        cityVo.setName(tCity.getName());
        countryService.reload(tCity.getId(), tCity.getType(), 1, cityVo);
        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:tCity:delete")
    public R delete(@RequestBody TCity tCity) {
        tCityService.removeById(tCity.getId());
        countryService.reload(tCity.getId(), tCity.getType(), 2, new CityVo());
        return R.ok();
    }

}
