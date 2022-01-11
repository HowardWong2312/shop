package io.dubai.admin.modules.other.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.dubai.admin.modules.sys.entity.SysDictEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 *
 * @author howard
 * @email admin@gmail.com
 * @date 2020-11-04 23:42:56
 */
@Data
@TableName("news")
public class NewsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 作者名字
	 */
	@NotBlank(message = "作者不能为空")
	private String author;

	/**
	 * 图片
	 */
	@NotBlank(message = "图片不能为空")
	private String headUrl;
	/**
	 * 标题
	 */
	@NotBlank(message = "标题不能为空")
	private String title;
	/**
	 * 摘要
	 */
	private String summary;
	/**
	 * 文本内容
	 */
	@NotBlank(message = "内容不能为空")
	private String body;
	/**
	 * 资讯类型
	 */
	@NotNull(message = "资讯类型不能为空")
	private Long newsType;

	@TableField(exist = false)
	private SysDictEntity dict;
	/**
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp createTime;
	/**
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp updateTime;

}
