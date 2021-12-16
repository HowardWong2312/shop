package io.dubai.modules.goods.entity.vo;

import io.dubai.modules.goods.entity.GoodsCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "奖品")
public class PrizeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    public PrizeVo(){

    }
    public PrizeVo(Integer id,Integer type){
        this.id = id;
        this.type = type;
    }

    public PrizeVo(Integer id, Integer type, BigDecimal score){
        this.id = id;
        this.type = type;
        this.score = score;
    }


    @ApiModelProperty(value = "奖品ID")
    private Integer id;

    @ApiModelProperty(value = "1：商品奖，2：积分奖，3：谢谢惠顾")
    private Integer type;

    @ApiModelProperty(value = "积分,type等于2时有效")
    private BigDecimal score;

}
