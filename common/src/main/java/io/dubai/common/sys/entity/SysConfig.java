/**
 *
 */

package io.dubai.common.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 系统配置信息
 *
 * @author mother fucker
 */
@Data
@TableName("sys_config")
public class SysConfig {

    @TableId
    private Long id;

    @NotBlank(message = "参数名不能为空")
    @TableField("paramKey")
    private String paramKey;

    @NotBlank(message = "参数值不能为空")
    @TableField("paramValue")
    private String paramValue;

    private String remark;

}
