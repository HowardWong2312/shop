package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.ShopGoodsOnebuyDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import io.dubai.admin.modules.goods.service.ShopGoodsOnebuyService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("shopGoodsOnebuyService")
public class ShopGoodsOnebuyServiceImpl extends ServiceImpl<ShopGoodsOnebuyDao, ShopGoodsOnebuy> implements ShopGoodsOnebuyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsOnebuyVo> page = new Query<ShopGoodsOnebuyVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page, params));
        return new PageUtils(page);
    }

    @Override
    public ShopGoodsOnebuyVo queryById(Long id) {
        return baseMapper.queryById(id);
    }

}
