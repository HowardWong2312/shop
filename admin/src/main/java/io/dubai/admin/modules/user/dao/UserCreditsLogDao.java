package io.dubai.admin.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.user.entity.UserCreditsLog;
import io.dubai.admin.modules.user.entity.vo.UserBalanceLogVo;
import io.dubai.admin.modules.user.entity.vo.UserCreditsLogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 资金记录
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
@Mapper
public interface UserCreditsLogDao extends BaseMapper<UserCreditsLog> {

    List<UserCreditsLogVo> queryPage(IPage page, Map<String, Object> params);

}
