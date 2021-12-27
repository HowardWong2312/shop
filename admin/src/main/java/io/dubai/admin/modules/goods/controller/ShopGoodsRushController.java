package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.entity.ShopGoods;
import io.dubai.admin.modules.goods.entity.ShopGoodsRush;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsRushVo;
import io.dubai.admin.modules.goods.service.ShopGoodsRushService;
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
 * 免费抢活动
 *
 * @author mother fucker
 * @name 免费抢活动
 * @date 2021-12-16 18:16:04
 */
@RestController
@RequestMapping("goods/shopGoodsRush")
public class ShopGoodsRushController {
    @Resource
    private ShopGoodsRushService shopGoodsRushService;

    @Resource
    private ShopGoodsService shopGoodsService;

    @GetMapping("/list")
    @RequiresPermissions("goods:shopGoodsRush:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = shopGoodsRushService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:shopGoodsRush:info")
    public R info(@PathVariable("id") Long id) {
        ShopGoodsRush shopGoodsRush = shopGoodsRushService.queryById(id);

        return R.ok().put("shopGoodsRush", shopGoodsRush);
    }

    @PostMapping("/check")
    @RequiresPermissions("goods:shopGoodsRush:check")
    public R check(@RequestBody ShopGoodsRushVo shopGoodsRushVo) {
        ShopGoodsRush bean = shopGoodsRushService.getById(shopGoodsRushVo.getId());
        ShopGoods goods = shopGoodsService.getById(shopGoodsRushVo.getGoodsId());
        if (shopGoodsRushVo.getStatus() == 1) {
            bean.setStatus(1);
            goods.setIsRush(1);
        }
        if (shopGoodsRushVo.getStatus() == 2) {
            bean.setStatus(2);
            goods.setIsRush(0);
        }
        shopGoodsService.updateById(goods);
        shopGoodsRushService.updateById(bean);
        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:shopGoodsRush:update")
    public R update(@RequestBody ShopGoodsRush shopGoodsRush) {
        ValidatorUtils.validateEntity(shopGoodsRush);
        shopGoodsRushService.updateById(shopGoodsRush);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:shopGoodsRush:delete")
    public R delete(@RequestBody Long[] ids) {
        List<ShopGoodsRush> list = shopGoodsRushService.listByIds(Arrays.asList(ids));
        list.forEach(s -> {
            ShopGoods goods = shopGoodsService.getById(s.getGoodsId());
            goods.setIsRush(0);
            shopGoodsService.updateById(goods);
            shopGoodsRushService.removeById(s.getId());
        });
        return R.ok();
    }

}
