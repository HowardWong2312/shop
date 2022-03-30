package io.dubai.admin.modules.goods.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.dubai.admin.modules.goods.entity.ShopGoodsOnebuyDetails;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 一元购记录
 * @date 2022-01-03 17:49:28
 */
@Data
public class ShopGoodsOnebuyDetailsVo extends ShopGoodsOnebuyDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userPhone;
    private String goodsName;


}
