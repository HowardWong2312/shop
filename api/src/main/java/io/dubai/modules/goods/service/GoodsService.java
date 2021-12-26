package io.dubai.modules.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.vo.GoodsVo;
import io.dubai.modules.goods.query.GoodsQuery;

import java.util.List;

/**
 * 算力产品
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
public interface GoodsService extends IService<Goods> {

    List<GoodsVo> queryFavoriteList(GoodsQuery query);

    PageUtils queryPage(GoodsQuery query);

    GoodsVo queryInfoByIdAndLanguageId(Long id,Long languageId);

    List<GoodsVo> queryListByUserIdAndLanguageId(Long userId, Long languageId);

    List<GoodsVo> queryListIssueByUserIdAndLanguageId(Long userId, Long languageId);
}

