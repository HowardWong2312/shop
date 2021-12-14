package io.dubai.modules.other.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.dubai.common.utils.R;
import io.dubai.modules.other.entity.Banner;
import io.dubai.modules.other.entity.Payment;
import io.dubai.modules.other.service.BannerService;
import io.dubai.modules.other.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 轮播图
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:29:29
 */
@RestController
@RequestMapping("other/banner")
@Api(tags = "轮播图")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @ApiOperation("列表")
    @GetMapping("/list")
    public R list() {
        return R.ok().put("list", bannerService.list(new QueryWrapper<Banner>().orderByDesc("order_num")));
    }



}
