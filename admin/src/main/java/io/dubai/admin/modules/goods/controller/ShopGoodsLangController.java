package io.dubai.admin.modules.goods.controller;

import java.util.Arrays;
import java.util.Map;

import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import io.dubai.admin.modules.goods.entity.ShopGoodsLang;
import io.dubai.admin.modules.goods.service.ShopGoodsLangService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;



/**
 * 商品标题
 *
 * @author mother fucker
 * @name 商品标题
 * @date 2021-12-17 14:05:29
 */
@RestController
@RequestMapping("goods/shopGoodsLang")
public class ShopGoodsLangController {
    @Resource
    private ShopGoodsLangService shopGoodsLangService;

    @GetMapping("/list")
        @RequiresPermissions("goods:shopGoodsLang:list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = shopGoodsLangService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
        @RequiresPermissions("goods:shopGoodsLang:info")
        public R info(@PathVariable("id") Long id){
        ShopGoodsLang shopGoodsLang = shopGoodsLangService.getById(id);

        return R.ok().put("shopGoodsLang", shopGoodsLang);
    }

    @PostMapping("/save")
        @RequiresPermissions("goods:shopGoodsLang:save")
        public R save(@RequestBody ShopGoodsLang shopGoodsLang){
        shopGoodsLangService.save(shopGoodsLang);

        return R.ok();
    }

    @PostMapping("/update")
        @RequiresPermissions("goods:shopGoodsLang:update")
        public R update(@RequestBody ShopGoodsLang shopGoodsLang){
        ValidatorUtils.validateEntity(shopGoodsLang);
        shopGoodsLangService.updateById(shopGoodsLang);
        
        return R.ok();
    }

    @DeleteMapping("/delete")
        @RequiresPermissions("goods:shopGoodsLang:delete")
        public R delete(@RequestBody Long[] ids){
        shopGoodsLangService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
