package io.dubai.admin.modules.other.controller;

import io.dubai.admin.modules.other.entity.ShopBanner;
import io.dubai.admin.modules.other.service.ShopBannerService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 轮播图
 *
 * @author mother fucker
 * @name 轮播图
 * @date 2022-01-04 19:01:44
 */
@RestController
@RequestMapping("other/shopBanner")
public class ShopBannerController {
    @Resource
    private ShopBannerService shopBannerService;

    @GetMapping("/list")
    @RequiresPermissions("other:shopBanner:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = shopBannerService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("other:shopBanner:info")
    public R info(@PathVariable("id") Long id) {
        ShopBanner shopBanner = shopBannerService.getById(id);

        return R.ok().put("shopBanner", shopBanner);
    }

    @PostMapping("/save")
    @RequiresPermissions("other:shopBanner:save")
    public R save(@RequestBody ShopBanner shopBanner) {
        shopBannerService.save(shopBanner);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("other:shopBanner:update")
    public R update(@RequestBody ShopBanner shopBanner) {
        ValidatorUtils.validateEntity(shopBanner);
        shopBannerService.updateById(shopBanner);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("other:shopBanner:delete")
    public R delete(@RequestBody Long[] ids) {
        shopBannerService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
