package io.dubai.admin.modules.goods.controller;

import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import io.dubai.admin.modules.goods.service.ShopGoodsOnebuyService;
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
    public R save(@RequestBody ShopGoodsOnebuyVo shopGoodsOnebuyVo) {
        ShopGoodsOnebuy bean = shopGoodsOnebuyService.getById(shopGoodsOnebuyVo.getId());
        if(shopGoodsOnebuyVo.getStatus() == 1){
            bean.setStatus(1);
        }
        if(shopGoodsOnebuyVo.getStatus() == 2){
            bean.setStatus(2);
        }
        shopGoodsOnebuyService.updateById(bean);
        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:shopGoodsOnebuy:update")
    public R update(@RequestBody ShopGoodsOnebuy shopGoodsOnebuy) {
        ValidatorUtils.validateEntity(shopGoodsOnebuy);
        shopGoodsOnebuyService.updateById(shopGoodsOnebuy);
        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:shopGoodsOnebuy:delete")
    public R delete(@RequestBody Long[] ids) {
        List<ShopGoodsOnebuy> list = shopGoodsOnebuyService.listByIds(Arrays.asList(ids));
        list.forEach(s->{
            shopGoodsOnebuyService.removeById(s.getId());
        });
        return R.ok();
    }

}
