package io.dubai.modules.goods.form;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:25:36
 */
@Data
@ApiModel(value = "一元购设置表单")
public class GoodsOneBuyForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "剩余份数")
    private Integer quantity;

    @ApiModelProperty(value = "到期时间")
    private String expireTime;

}
