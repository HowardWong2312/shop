package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.goods.dao.GoodsImgDao;
import io.dubai.modules.goods.entity.GoodsImg;
import io.dubai.modules.goods.service.GoodsImgService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("goodsImgService")
public class GoodsImgServiceImpl extends ServiceImpl<GoodsImgDao, GoodsImg> implements GoodsImgService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GoodsImg> page = this.page(
                new Query<GoodsImg>().getPage(params),
                new QueryWrapper<GoodsImg>()
        );

        return new PageUtils(page);
    }

}
