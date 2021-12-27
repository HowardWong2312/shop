package io.dubai.modules.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.annotation.Login;
import io.dubai.common.annotation.LoginUser;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import io.dubai.common.validator.ValidatorUtils;
import io.dubai.modules.goods.entity.GoodsCategory;
import io.dubai.modules.goods.entity.vo.GoodsCategoryVo;
import io.dubai.modules.goods.service.GoodsCategoryService;
import io.dubai.modules.other.entity.Language;
import io.dubai.modules.other.service.LanguageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品分类
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-15 18:18:50
 */
@RestController
@RequestMapping("goods/category")
@Api(tags = "商品分类")
public class GoodsCategoryController {

    @Resource
    private GoodsCategoryService goodsCategoryService;

    @Resource
    private LanguageService languageService;

    @ApiOperation("根据父级查子级列表")
    @GetMapping("/list/{parentId}")
    @Login
    public R list(@PathVariable Long parentId, @ApiIgnore @LoginUser UserInfo userInfo) {
        Long languageId = 1L;
        Language language = languageService.queryByName(userInfo.getLanguage());
        if(language != null){
            languageId = language.getId();
        }
        List<GoodsCategoryVo> list = goodsCategoryService.queryListByParentIdAndLanguageId(parentId,languageId);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setKids(goodsCategoryService.queryListByParentIdAndLanguageId(list.get(i).getId(),languageId));
        }
        return R.ok().put("list", list);
    }

    @ApiOperation("根据父级和语言查子级列表")
    @GetMapping("/list/{parentId}/{languageId}")
    @Login
    public R list(@PathVariable Long parentId,@PathVariable Long languageId) {
        List<GoodsCategoryVo> list = goodsCategoryService.queryListByParentIdAndLanguageId(parentId,languageId);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setKids(goodsCategoryService.queryListByParentIdAndLanguageId(list.get(i).getId(),languageId));
        }
        return R.ok().put("list", list);
    }

}
