package io.dubai.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@TableName("shop_goods_group_record")
@ApiModel(value = "拼团记录")
public class GoodsGroupRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "")
    @TableId
    private Long id;

    @ApiModelProperty(value = "拼团商品ID")
    private Long goodsGroupId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "仍需人数")
    private Integer stillNeed;

    @ApiModelProperty(value = "总需人数")
    private Integer userNum;

    @ApiModelProperty(value = "状态(0:拼团中,1:已结束)")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updateTime;


}
