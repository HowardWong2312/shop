package io.dubai.admin.modules.goods.service.impl;

import io.dubai.admin.modules.goods.entity.vo.ShopGoodsOnebuyVo;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;

import io.dubai.admin.modules.goods.dao.ShopGoodsOnebuyDao;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import io.dubai.admin.modules.goods.service.ShopGoodsOnebuyService;


@Service("shopGoodsOnebuyService")
public class ShopGoodsOnebuyServiceImpl extends ServiceImpl<ShopGoodsOnebuyDao, ShopGoodsOnebuy> implements ShopGoodsOnebuyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopGoodsOnebuyVo> page = new Query<ShopGoodsOnebuyVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

    @Override
    public ShopGoodsOnebuyVo queryById(Long id) {
        return baseMapper.queryById(id);
    }

}
