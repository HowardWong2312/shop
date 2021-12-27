package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.service.ShopGoodsOrderService;
import io.dubai.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 商品订单
 *
 * @author mother fucker
 * @name 商品订单
 * @date 2021-12-25 18:14:19
 */
@RestController
@RequestMapping("goods/shopGoodsOrder")
public class ShopGoodsOrderController {
    @Resource
    private ShopGoodsOrderService shopGoodsOrderService;

    @GetMapping("/list")
    @RequiresPermissions("goods:shopGoodsOrder:list")
    public R list(@RequestParam Map<String, Object> params) {
        return R.ok().put("page", shopGoodsOrderService.queryPage(params));
    }


    @GetMapping("/info/{orderCode}")
    @RequiresPermissions("goods:shopGoodsOrder:info")
    public R info(@PathVariable("orderCode") String orderCode) {
        return R.ok().put("shopGoodsOrder", shopGoodsOrderService.queryById(orderCode));
    }

}
