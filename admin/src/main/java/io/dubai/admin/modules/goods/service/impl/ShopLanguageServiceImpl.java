package io.dubai.admin.modules.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.goods.dao.ShopLanguageDao;
import io.dubai.admin.modules.goods.entity.ShopLanguage;
import io.dubai.admin.modules.goods.service.ShopLanguageService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("shopLanguageService")
public class ShopLanguageServiceImpl extends ServiceImpl<ShopLanguageDao, ShopLanguage> implements ShopLanguageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShopLanguage> page = this.page(
                new Query<ShopLanguage>().getPage(params),
                new QueryWrapper<ShopLanguage>()
        );

        return new PageUtils(page);
    }

}
