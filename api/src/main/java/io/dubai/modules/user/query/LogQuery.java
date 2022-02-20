package io.dubai.modules.user.query;

import io.dubai.common.utils.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("资金记录或者积分记录查询参数")
public class LogQuery extends Query {

    @ApiModelProperty("类型")
    private Integer status;

    @ApiModelProperty("账单方向")
    private Integer type;

    @ApiModelProperty("用户ID，不用传")
    private Long userId;

}
