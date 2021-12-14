package io.dubai.common.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class JsonResultVO {

    private JSONObject data;

    private String message;

    private Integer code;

    private Boolean status;

}
