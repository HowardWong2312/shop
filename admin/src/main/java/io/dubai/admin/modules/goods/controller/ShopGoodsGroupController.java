package io.dubai.admin.modules.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.dubai.admin.modules.goods.entity.ShopGoodsGroup;
import io.dubai.admin.modules.goods.service.ShopGoodsGroupService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 拼团商品
 *
 * @author mother fucker
 * @name 拼团商品
 * @date 2021-12-19 20:21:24
 */
@RestController
@RequestMapping("goods/shopGoodsGroup")
public class ShopGoodsGroupController {
    @Resource
    private ShopGoodsGroupService shopGoodsGroupService;

    @GetMapping("/list")
    @RequiresPermissions("goods:shopGoodsGroup:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = shopGoodsGroupService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:shopGoodsGroup:info")
    public R info(@PathVariable("id") Long id) {
        ShopGoodsGroup shopGoodsGroup = shopGoodsGroupService.getOne(new QueryWrapper<ShopGoodsGroup>().eq("goods_id",id));

        return R.ok().put("shopGoodsGroup", shopGoodsGroup);
    }

    @PostMapping("/save")
    @RequiresPermissions("goods:shopGoodsGroup:save")
    public R save(@RequestBody ShopGoodsGroup shopGoodsGroup) {
        shopGoodsGroupService.save(shopGoodsGroup);

        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:shopGoodsGroup:update")
    public R update(@RequestBody ShopGoodsGroup shopGoodsGroup) {
        ValidatorUtils.validateEntity(shopGoodsGroup);
        shopGoodsGroupService.updateById(shopGoodsGroup);

        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:shopGoodsGroup:delete")
    public R delete(@RequestBody Long[] ids) {
        shopGoodsGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
