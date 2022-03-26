package io.dubai.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@TableName("shop_goods_order")
@ApiModel(value = "商品订单")
public class GoodsOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编号")
    @TableId(type = IdType.INPUT)
    private String orderCode;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "支付方式ID")
    private Long paymentId;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal amount;

    private String receiverName;
    private String receiverPhone;
    private String receiverProvince;
    private String receiverCity;
    private String receiverArea;
    private String receiverAddress;

    @ApiModelProperty(value = "物流公司")
    private String logistics;

    @ApiModelProperty(value = "物流单号")
    private String logisticsCode;

    @ApiModelProperty(value = "用户退货理由")
    private String returnReason;

    @ApiModelProperty(value = "商家备注-针对退货")
    private String remark;

    @ApiModelProperty(value = "状态(0待付款,1待分享,2,待确认,3待发货,4待收货,5交易完成,6交易取消,7:申请退货中,8:退货成功,9:退货失败)")
    private Integer status;

    @ApiModelProperty(value = "软删除")
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableLogic
    private Integer isDel;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Version
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
    @TableField(fill = FieldFill.UPDATE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updateTime;


}
