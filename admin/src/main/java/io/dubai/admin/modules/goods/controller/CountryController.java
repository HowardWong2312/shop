package io.dubai.admin.modules.goods.controller;

import java.util.Arrays;
import java.util.Map;

import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import io.dubai.admin.modules.goods.entity.Country;
import io.dubai.admin.modules.goods.service.CountryService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;



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
    private CountryService CountryService;

    @GetMapping("/list")
        @RequiresPermissions("goods:Country:list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = CountryService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
        @RequiresPermissions("goods:Country:info")
        public R info(@PathVariable("id") Integer id){
        Country Country = CountryService.getById(id);

        return R.ok().put("Country", Country);
    }

    @PostMapping("/save")
        @RequiresPermissions("goods:Country:save")
        public R save(@RequestBody Country Country){
        CountryService.save(Country);

        return R.ok();
    }

    @PostMapping("/update")
        @RequiresPermissions("goods:Country:update")
        public R update(@RequestBody Country Country){
        ValidatorUtils.validateEntity(Country);
        CountryService.updateById(Country);

        return R.ok();
    }

    @DeleteMapping("/delete")
        @RequiresPermissions("goods:Country:delete")
        public R delete(@RequestBody Integer[] ids){
        CountryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
