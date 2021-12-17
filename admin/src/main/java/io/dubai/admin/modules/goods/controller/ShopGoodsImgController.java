package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.entity.ShopGoodsImg;
import io.dubai.admin.modules.goods.service.ShopGoodsImgService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 商品图片
 *
 * @author mother fucker
 * @name 商品图片
 * @date 2021-12-17 14:05:29
 */
@RestController
@RequestMapping("goods/shopGoodsImg")
public class ShopGoodsImgController {
    @Resource
    private ShopGoodsImgService shopGoodsImgService;

    @GetMapping("/list")
    @RequiresPermissions("goods:shopGoodsImg:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = shopGoodsImgService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:shopGoodsImg:info")
    public R info(@PathVariable("id") Long id) {
        ShopGoodsImg shopGoodsImg = shopGoodsImgService.getById(id);

        return R.ok().put("shopGoodsImg", shopGoodsImg);
    }

    @PostMapping("/save")
    @RequiresPermissions("goods:shopGoodsImg:save")
    public R save(@RequestBody ShopGoodsImg shopGoodsImg) {
        shopGoodsImgService.save(shopGoodsImg);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:shopGoodsImg:update")
    public R update(@RequestBody ShopGoodsImg shopGoodsImg) {
        ValidatorUtils.validateEntity(shopGoodsImg);
        shopGoodsImgService.updateById(shopGoodsImg);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:shopGoodsImg:delete")
    public R delete(@RequestBody Long[] ids) {
        shopGoodsImgService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
