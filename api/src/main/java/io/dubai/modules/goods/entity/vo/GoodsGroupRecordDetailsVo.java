package io.dubai.modules.goods.entity.vo;

import io.dubai.modules.goods.entity.GoodsGroupRecord;
import io.dubai.modules.goods.entity.GoodsGroupRecordDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Howard
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "拼团列表Vo")
public class GoodsGroupRecordDetailsVo extends GoodsGroupRecordDetails implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "参团用户名称")
    private String userName;

    @ApiModelProperty(value = "参团用户头像")
    private String userFaceImg;


}
