package io.dubai.admin.modules.goods.controller;

import java.util.Arrays;
import java.util.Map;

import io.dubai.admin.modules.goods.service.ShopLanguageService;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import io.dubai.admin.modules.goods.entity.ShopLanguage;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;


/**
 * 语言
 *
 * @author mother fucker
 * @name 语言
 * @date 2021-12-15 20:01:22
 */
@RestController
@RequestMapping("goods/shopLanguage")
public class ShopLanguageController {
    @Resource
    private ShopLanguageService shopLanguageService;

    @GetMapping("/list")
    @RequiresPermissions("goods:shopLanguage:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = shopLanguageService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:shopLanguage:info")
    public R info(@PathVariable("id") Long id) {
        ShopLanguage shopLanguage = shopLanguageService.getById(id);

        return R.ok().put("shopLanguage", shopLanguage);
    }

    @PostMapping("/save")
    @RequiresPermissions("goods:shopLanguage:save")
    public R save(@RequestBody ShopLanguage shopLanguage) {
        shopLanguageService.save(shopLanguage);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:shopLanguage:update")
    public R update(@RequestBody ShopLanguage shopLanguage) {
        ValidatorUtils.validateEntity(shopLanguage);
        shopLanguageService.updateById(shopLanguage);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:shopLanguage:delete")
    public R delete(@RequestBody Long[] ids) {
        shopLanguageService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
