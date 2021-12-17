package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.service.ShopGoodsService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 商品
 *
 * @author mother fucker
 * @name 商品
 * @date 2021-12-17 14:05:29
 */
@RestController
@RequestMapping("goods/shopGoods")
public class ShopGoodsController {
    @Resource
    private ShopGoodsService shopGoodsService;

    @GetMapping("/list")
    @RequiresPermissions("goods:shopGoods:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = shopGoodsService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:shopGoods:info")
    public R info(@PathVariable("id") Long id) {
        ShopGoods shopGoods = shopGoodsService.getById(id);

        return R.ok().put("shopGoods", shopGoods);
    }

    @PostMapping("/save")
    @RequiresPermissions("goods:shopGoods:save")
    public R save(@RequestBody ShopGoods shopGoods) {
        shopGoodsService.save(shopGoods);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:shopGoods:update")
    public R update(@RequestBody ShopGoods shopGoods) {
        ValidatorUtils.validateEntity(shopGoods);
        shopGoodsService.updateById(shopGoods);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:shopGoods:delete")
    public R delete(@RequestBody Long[] ids) {
        shopGoodsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
