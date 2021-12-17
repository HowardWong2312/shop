package io.dubai.admin.modules.goods.entity.vo;

import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import io.dubai.admin.modules.goods.entity.ShopGoodsRush;
import lombok.Data;

@Data
public class ShopGoodsRushVo extends ShopGoodsRush {

    private String goodsName;

    private String merchantName;

    private String statusValue;

    private String statusColor;
}
