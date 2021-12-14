package io.dubai.modules.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.modules.goods.dao.UserFavoriteGoodsDao;
import io.dubai.modules.goods.entity.UserFavoriteGoods;
import io.dubai.modules.goods.service.UserFavoriteGoodsService;
import org.springframework.stereotype.Service;


@Service("userFavoriteGoodsService")
public class UserFavoriteGoodsServiceImpl extends ServiceImpl<UserFavoriteGoodsDao, UserFavoriteGoods> implements UserFavoriteGoodsService {

}
