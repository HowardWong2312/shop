package io.dubai.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.goods.entity.GoodsCategory;
import io.dubai.modules.goods.entity.vo.GoodsCategoryVo;
import io.dubai.modules.goods.query.GoodsQuery;

import java.util.List;
import java.util.Map;

/**
 * 商品分类
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-15 18:18:50
 */
public interface GoodsCategoryService extends IService<GoodsCategory> {

    List<GoodsCategoryVo> queryListByParentIdAndLanguageId(Long parentId, String languageId);

    GoodsCategoryVo queryByIdAndLanguageId(Long id, String languageId);

    List<Long> queryIterativeIdsByParentId(Long parentId);

}

