package io.dubai.modules.goods.entity.vo;

import io.dubai.modules.goods.entity.Goods;
import io.dubai.modules.goods.entity.GoodsGroup;
import io.dubai.modules.goods.entity.GoodsGroupRecord;
import io.dubai.modules.goods.entity.GoodsImg;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "拼团列表Vo")
public class GoodsGroupRecordVo extends GoodsGroupRecord implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "发起人名称")
    private String userName;

    @ApiModelProperty(value = "发起人头像")
    private String userFaceImg;

    @ApiModelProperty(value = "拼团有效期(小时)")
    private Integer period;

    @ApiModelProperty(value = "截止时间戳")
    private Long periodTime;


}
