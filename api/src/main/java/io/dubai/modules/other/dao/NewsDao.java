package io.dubai.modules.other.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.other.entity.NewsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author howard
 * @email admin@gmail.com
 * @date 2020-11-04 23:42:56
 */
@Mapper
public interface NewsDao extends BaseMapper<NewsEntity> {
	
}
