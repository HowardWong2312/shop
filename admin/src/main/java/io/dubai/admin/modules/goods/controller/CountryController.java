package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.entity.Country;
import io.dubai.admin.modules.goods.entity.vo.CityVo;
import io.dubai.admin.modules.goods.service.CountryService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.utils.RedisKeys;
import io.dubai.common.utils.RedisUtils;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 国家表
 *
 * @author mother fucker
 * @name 国家表
 * @date 2021-12-19 14:07:37
 */
@RestController
@RequestMapping("goods/Country")
public class CountryController {
    @Resource
    private CountryService countryService;

    @Resource
    private RedisUtils redisUtils;

    @GetMapping("/list")
    @RequiresPermissions("goods:Country:list")
    public List list(@RequestParam Map<String, Object> params) {

        return countryService.queryPage(params);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:Country:info")
    public R info(@PathVariable("id") Integer id) {
        Country country = countryService.getById(id);

        return R.ok().put("Country", country);
    }

    @PostMapping("/save")
    @RequiresPermissions("goods:Country:save")
    public R save(@RequestBody Country country) {
        countryService.save(country);
        countryService.reload(country.getId(), country.getType(), 0, new CityVo(country.getId(), country.getParentId(), country.getName(), country.getType()));

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:Country:update")
    public R update(@RequestBody Country country) {
        ValidatorUtils.validateEntity(country);
        countryService.updateById(country);

        CityVo cityVo = new CityVo();
        cityVo.setName(country.getName());
        countryService.reload(country.getId(), country.getType(), 1, cityVo);
        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:Country:delete")
    public R delete(@RequestBody Country country) {
        countryService.removeById(country.getId());
        countryService.reload(country.getId(), country.getType(), 2, new CityVo());
        return R.ok();
    }

}
