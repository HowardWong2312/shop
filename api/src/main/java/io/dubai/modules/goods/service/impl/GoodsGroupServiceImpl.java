package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.goods.dao.GoodsGroupDao;
import io.dubai.modules.goods.entity.GoodsGroup;
import io.dubai.modules.goods.service.GoodsGroupService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("goodsGroupService")
public class GoodsGroupServiceImpl extends ServiceImpl<GoodsGroupDao, GoodsGroup> implements GoodsGroupService {



}
