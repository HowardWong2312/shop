package io.dubai.admin.modules.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.goods.entity.Country;
import org.apache.ibatis.annotations.Mapper;

/**
 * 国家表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-19 14:07:37
 */
@Mapper
public interface CountryDao extends BaseMapper<Country> {

}
