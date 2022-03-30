package io.dubai.admin.modules.goods.entity.vo;

import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import lombok.Data;

@Data
public class ShopGoodsOnebuyVo extends ShopGoodsOnebuy {

    private String goodsName;

    private String merchantPhone;

    private String statusValue;

    private String statusColor;

}
