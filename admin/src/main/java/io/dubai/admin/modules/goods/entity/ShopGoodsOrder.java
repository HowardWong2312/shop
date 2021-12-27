package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @author mother fucker
 * @name 商品订单
 * @date 2021-12-25 18:14:19
 */
@Data
@TableName("shop_goods_order")
public class ShopGoodsOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    //订单编号
    @TableId
    private String orderCode;

    //用户ID
    private Long userId;

    //商品ID
    private Long goodsId;

    //数量
    private Integer quantity;

    //支付方式
    private Long paymentId;

    //订单金额
    private BigDecimal amount;

    //收货人
    private String receiverName;

    //
    private String receiverPhone;

    //
    private String receiverProvince;

    //
    private String receiverCity;

    //
    private String receiverArea;

    //
    private String receiverAddress;

    //物流公司
    private String logistics;

    //物流单号
    private String logisticsCode;

    //用户退货理由
    private String returnReason;

    //商家备注-针对退货
    private String remark;

    //状态(0待付款,1待分享,2,待确认,3待发货,4已发货/待收货,5交易完成,6交易取消,7:申请退货中,8:退货成功,9:退货失败)
    private Integer status;

    //
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDel;

    //
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
