package io.dubai.admin.modules.other.controller;

import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import io.dubai.admin.modules.other.entity.NewsEntity;
import io.dubai.admin.modules.other.service.NewsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 
 *
 * @author howard
 * @email admin@gmail.com
 * @date 2020-11-04 23:42:56
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource
    private NewsService newsService;


    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("news:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = newsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("news:info")
    public R info(@PathVariable("id") Long id){
        NewsEntity news = newsService.getById(id);

        return R.ok().put("news", news);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("news:save")
    public R save(NewsEntity news){
        ValidatorUtils.validateEntity(news);
        newsService.save(news);
        return R.ok();
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("news:update")
    public R update(NewsEntity news){
        ValidatorUtils.validateEntity(news);
        news.setUpdateTime(null);
        newsService.updateById(news);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("news:delete")
    public R delete(@RequestBody Long[] ids){
        newsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
