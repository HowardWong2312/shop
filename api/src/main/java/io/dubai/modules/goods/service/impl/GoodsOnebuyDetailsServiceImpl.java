package io.dubai.modules.goods.service.impl;

import io.dubai.modules.goods.dao.GoodsOnebuyDetailsDao;
import io.dubai.modules.goods.entity.GoodsOnebuyDetails;
import io.dubai.modules.goods.service.GoodsOnebuyDetailsService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;



@Service("goodsOnebuyDetailsService")
public class GoodsOnebuyDetailsServiceImpl extends ServiceImpl<GoodsOnebuyDetailsDao, GoodsOnebuyDetails> implements GoodsOnebuyDetailsService {


}
