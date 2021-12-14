package io.dubai.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.dubai.common.xss.SQLFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 查询参数
 *
 * @author howard
 */
@Data
@ApiModel("分页参数")
public class Query<T> {

    @ApiModelProperty(value = "页码",example = "1")
    private long pageNum = 1;

    @ApiModelProperty(value = "每页条数",example = "10")
    private long pageSize = 10;

    @ApiModelProperty(value = "排序字段,对应数据库字段",example = "create_time")
    private String orderField = "create_time";

    @ApiModelProperty(value = "排序方式,true升序，false降序",example = "false")
    private boolean asc = false;

    public IPage<T> getPage() {
        Page<T> pager = new Page<>(pageNum, pageSize);
        //默认排序
        if(!"".equalsIgnoreCase(orderField)){
            if(asc) {
                pager.setAsc(orderField);
            }else {
                pager.setDesc(orderField);
            }
        }
        return pager;
    }

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(Constant.PAGE) != null){
            curPage = Long.parseLong(params.get(Constant.PAGE).toString());
        }
        if(params.get(Constant.LIMIT) != null){
            limit = Long.parseLong(params.get(Constant.LIMIT).toString());
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String)params.get(Constant.ORDER_FIELD));
        String order = (String)params.get(Constant.ORDER);

        //前端字段排序
        if(StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)){
            if(Constant.ASC.equalsIgnoreCase(order)) {
                return page.setAsc(orderField);
            }else {
                return page.setDesc(orderField);
            }
        }

        //默认排序
        if(isAsc) {
            page.setAsc(defaultOrderField);
        }else {
            page.setDesc(defaultOrderField);
        }

        return page;
    }
}
