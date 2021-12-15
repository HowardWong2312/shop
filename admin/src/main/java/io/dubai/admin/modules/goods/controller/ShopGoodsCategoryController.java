package io.dubai.admin.modules.goods.controller;

import java.util.Arrays;
import java.util.Map;

import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import io.dubai.admin.modules.goods.entity.ShopGoodsCategory;
import io.dubai.admin.modules.goods.service.ShopGoodsCategoryService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;



/**
 * 商品分类
 *
 * @author mother fucker
 * @name 商品分类
 * @date 2021-12-15 15:03:45
 */
@RestController
@RequestMapping("goods/shopGoodsCategory")
public class ShopGoodsCategoryController {
    @Resource
    private ShopGoodsCategoryService shopGoodsCategoryService;

    @GetMapping("/list")
        @RequiresPermissions("goods:shopGoodsCategory:list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = shopGoodsCategoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
        @RequiresPermissions("goods:shopGoodsCategory:info")
        public R info(@PathVariable("id") Long id){
        ShopGoodsCategory shopGoodsCategory = shopGoodsCategoryService.getById(id);

        return R.ok().put("shopGoodsCategory", shopGoodsCategory);
    }

    @PostMapping("/save")
        @RequiresPermissions("goods:shopGoodsCategory:save")
        public R save(@RequestBody ShopGoodsCategory shopGoodsCategory){
        shopGoodsCategoryService.save(shopGoodsCategory);

        return R.ok();
    }

    @PostMapping("/update")
        @RequiresPermissions("goods:shopGoodsCategory:update")
        public R update(@RequestBody ShopGoodsCategory shopGoodsCategory){
        ValidatorUtils.validateEntity(shopGoodsCategory);
        shopGoodsCategoryService.updateById(shopGoodsCategory);

        return R.ok();
    }

    @DeleteMapping("/delete")
        @RequiresPermissions("goods:shopGoodsCategory:delete")
        public R delete(@RequestBody Long[] ids){
        shopGoodsCategoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}