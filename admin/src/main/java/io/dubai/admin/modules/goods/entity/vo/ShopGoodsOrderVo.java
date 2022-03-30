package io.dubai.admin.modules.goods.entity.vo;

import io.dubai.admin.modules.goods.entity.ShopGoodsOrder;
import lombok.Data;

import java.io.Serializable;


/**
 * @author mother fucker
 * @name 商品订单
 * @date 2021-12-25 18:14:19
 */
@Data
public class ShopGoodsOrderVo extends ShopGoodsOrder implements Serializable {
    private static final long serialVersionUID = 1L;


    private String goodsName;

    private String merchantPhone;

    private String userPhone;

    private String statusValue;

}
