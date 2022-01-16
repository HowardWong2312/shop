package io.dubai.admin.modules.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.dubai.admin.modules.goods.entity.TProvince;
import io.dubai.admin.modules.goods.entity.vo.CityVo;
import io.dubai.admin.modules.goods.service.CountryService;
import io.dubai.admin.modules.goods.service.TProvinceService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.utils.StringUtils;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


/**
 * 省级表
 *
 * @author mother fucker
 * @name 省级表
 * @date 2022-01-14 18:51:41
 */
@RestController
@RequestMapping("goods/tProvince")
public class TProvinceController {
    @Resource
    private TProvinceService tProvinceService;

    @Resource
    private CountryService countryService;

    @GetMapping("/list")
    @RequiresPermissions("goods:tProvince:list")
    public List<TProvince> list(@RequestParam Map<String, Object> params) {
        QueryWrapper<TProvince> eq = new QueryWrapper<TProvince>();
        if (!StringUtils.isEmpty(params.get("countryId"))) {
            eq.eq("countryId", params.get("countryId"));
        }
        return tProvinceService.list(eq);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("goods:tProvince:info")
    public R info(@PathVariable("id") Integer id) {
        TProvince tProvince = tProvinceService.getById(id);

        return R.ok().put("tProvince", tProvince);
    }

    @PostMapping("/save")
    @RequiresPermissions("goods:tProvince:save")
    public R save(@RequestBody TProvince tProvince) {
        tProvinceService.save(tProvince);
        countryService.reload(tProvince.getId(), tProvince.getType(), 0, new CityVo(tProvince.getId(), tProvince.getParentId(), tProvince.getName(), tProvince.getType()));
        return R.ok();
    }

    @PostMapping("/update")
    @RequiresPermissions("goods:tProvince:update")
    public R update(@RequestBody TProvince tProvince) {
        ValidatorUtils.validateEntity(tProvince);
        tProvinceService.updateById(tProvince);
        CityVo cityVo = new CityVo();
        cityVo.setName(tProvince.getName());
        countryService.reload(tProvince.getId(), tProvince.getType(), 1, cityVo);
        return R.ok();
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("goods:tProvince:delete")
    public R delete(@RequestBody TProvince tProvince) {
        tProvinceService.removeById(tProvince.getId());
        countryService.reload(tProvince.getId(), tProvince.getType(), 2, new CityVo());

        return R.ok();
    }

}
