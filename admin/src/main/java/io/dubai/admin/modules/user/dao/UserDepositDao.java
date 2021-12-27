package io.dubai.admin.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.user.entity.UserDeposit;
import io.dubai.admin.modules.user.entity.vo.UserDepositVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 提现申请表
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-14 18:27:22
 */
@Mapper
public interface UserDepositDao extends BaseMapper<UserDeposit> {

    List<UserDepositVo> queryPage(IPage page, Map<String, Object> params);

}
