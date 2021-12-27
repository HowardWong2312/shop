package io.dubai.admin.modules.other.controller;

import io.dubai.admin.modules.other.entity.ShowVideo;
import io.dubai.admin.modules.other.service.ShowVideoService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 我的视频
 *
 * @author mother fucker
 * @name 我的视频
 * @date 2021-12-25 19:13:36
 */
@RestController
@RequestMapping("other/showVideo")
public class ShowVideoController {
    @Resource
    private ShowVideoService showVideoService;

    @GetMapping("/list")
    @RequiresPermissions("other:showVideo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = showVideoService.queryPage(params);

        return R.ok().put("page", page);
    }


    @GetMapping("/info/{id}")
    @RequiresPermissions("other:showVideo:info")
    public R info(@PathVariable("id") Integer id) {
        ShowVideo showVideo = showVideoService.getById(id);
        return R.ok().put("showVideo", showVideo);
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("other:showVideo:delete")
    public R delete(@RequestBody Integer[] ids) {
        showVideoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
