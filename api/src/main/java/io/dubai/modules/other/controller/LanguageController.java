package io.dubai.modules.other.controller;

import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import io.dubai.modules.other.entity.Language;
import io.dubai.modules.other.service.LanguageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 语言
 *
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-11 20:00:11
 */
@RestController
@RequestMapping("other/language")
@Api(tags = "语言")
public class LanguageController {
    @Resource
    private LanguageService languageService;

    @ApiOperation("列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        return R.ok().put("list", languageService.list());
    }


}
