package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.modules.goods.dao.GoodsDao;
import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.vo.GoodsVo;
import io.dubai.modules.goods.query.GoodsQuery;
import io.dubai.modules.goods.service.GoodsService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Override
    public List<GoodsVo> queryFavoriteList(GoodsQuery query) {
        return baseMapper.queryFavoriteList(query);
    }

    @Override
    public PageUtils queryPage(GoodsQuery query) {
        IPage<GoodsVo> page = query.getPage();
        page.setRecords(baseMapper.queryPage(page, query));
        return new PageUtils(page);
    }

    @Override
    public GoodsVo queryInfoByIdAndLanguageId(Long id,Long languageId) {
        GoodsVo goodsVo = baseMapper.queryInfoByIdAndLanguageId(id,languageId);
        return goodsVo;
    }

    @Override
    public List<GoodsVo> queryListByUserIdAndLanguageId(Long userId, Long languageId) {
        return baseMapper.queryListByUserIdAndLanguageId(userId,languageId);
    }

}
