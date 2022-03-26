package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuyDetails;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyDetailsVo;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import io.dubai.admin.modules.goods.service.ShopGoodsOnebuyDetailsService;
import io.dubai.admin.modules.goods.service.ShopGoodsOnebuyService;
import io.dubai.admin.modules.goods.service.ShopGoodsService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 一元购活动
 *
 * @author mother fucker
 * @name 一元购活动
 * @date 2021-12-16 18:16:04
 */
@RestController
@RequestMapping("goods/shopGoodsOnebuy")
public class ShopGoodsOnebuyController {

    @Resource
    private ShopGoodsOnebuyService shopGoodsOnebuyService;

    @Resource
    private ShopGoodsOnebuyDetailsService shopGoodsOnebuyDetailsService;

    @Resource
    private ShopGoodsService shopGoodsService;

    @GetMapping("/list")
    @RequiresPermissions("goods:shopGoodsOnebuy:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = shopGoodsOnebuyService.queryPage(params);
        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:shopGoodsOnebuy:info")
    public R info(@PathVariable("id") Long id) {
        ShopGoodsOnebuy shopGoodsOnebuy = shopGoodsOnebuyService.queryById(id);
        return R.ok().put("shopGoodsOnebuy", shopGoodsOnebuy);
    }

    @PostMapping("/check")
    @RequiresPermissions("goods:shopGoodsOnebuy:check")
    public R check(@RequestBody ShopGoodsOnebuyVo shopGoodsOnebuyVo) {
        ShopGoodsOnebuy goodsOnebuy = shopGoodsOnebuyService.getById(shopGoodsOnebuyVo.getId());
        ShopGoods goods = shopGoodsService.getById(shopGoodsOnebuyVo.getGoodsId());
        if (shopGoodsOnebuyVo.getStatus() == 1) {
            goodsOnebuy.setStatus(1);
            goods.setIsOneBuy(1);
            if(goods.getStock()>goodsOnebuy.getQuantity()){
                goods.setStock(goods.getStock()-goodsOnebuy.getQuantity());
            }else{
                goods.setStock(0);
            }
        }
        if (shopGoodsOnebuyVo.getStatus() == 2) {
            goodsOnebuy.setStatus(2);
            goods.setIsOneBuy(0);
        }
        shopGoodsService.updateById(goods);
        shopGoodsOnebuyService.updateById(goodsOnebuy);
        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:shopGoodsOnebuy:update")
    public R update(@RequestBody ShopGoodsOnebuy shopGoodsOnebuy) {
        ValidatorUtils.validateEntity(shopGoodsOnebuy);
        shopGoodsOnebuyService.updateById(shopGoodsOnebuy);
        return R.ok();
    }

    @GetMapping("/details/{goodsOnebuyId}")
    @RequiresPermissions("goods:shopGoodsOnebuy:details")
    public R details(@PathVariable("goodsOnebuyId") Long goodsOnebuyId) {
        List<ShopGoodsOnebuyDetailsVo> list = shopGoodsOnebuyDetailsService.queryList(goodsOnebuyId);
        return R.ok().put("goodsOnebuyDetailsList",list);
    }

}
