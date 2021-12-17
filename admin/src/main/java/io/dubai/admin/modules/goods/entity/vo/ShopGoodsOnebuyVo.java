package io.dubai.admin.modules.goods.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuy;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopGoodsOnebuyVo extends ShopGoodsOnebuy {

    private String goodsName;

    private String merchantName;

    private String statusValue;

    private String statusColor;

}
