package io.dubai.admin.modules.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author mother fucker
 * @name 商品标题
 * @date 2021-12-17 14:05:29
 */
@Data
@TableName("shop_goods_lang")
public class ShopGoodsLang implements Serializable {
	private static final long serialVersionUID = 1L;

	//
		@TableId
								private Long id;

	//商品ID
								private Long goodsId;

	//商品名称
								private String title;

	//logo
								private String logoUrl;

	//描述
								private String description;

	//语言ID
								private Long languageId;

	//
			@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
				@TableField(fill = FieldFill.INSERT)
					private LocalDateTime createTime;

	//
			@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
							@TableField(fill = FieldFill.UPDATE)
		private LocalDateTime updateTime;


}
