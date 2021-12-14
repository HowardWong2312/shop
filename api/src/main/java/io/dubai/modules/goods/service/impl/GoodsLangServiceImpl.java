package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.goods.dao.GoodsLangDao;
import io.dubai.modules.goods.entity.GoodsLang;
import io.dubai.modules.goods.service.GoodsLangService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("goodsLangService")
public class GoodsLangServiceImpl extends ServiceImpl<GoodsLangDao, GoodsLang> implements GoodsLangService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GoodsLang> page = this.page(
                new Query<GoodsLang>().getPage(params),
                new QueryWrapper<GoodsLang>()
        );

        return new PageUtils(page);
    }

}
