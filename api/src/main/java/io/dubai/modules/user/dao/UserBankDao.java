package io.dubai.modules.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.modules.user.entity.UserBank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户提现
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-10-07 16:28:10
 */
@Mapper
public interface UserBankDao extends BaseMapper<UserBank> {

    List<UserBank> queryByUserIdAndLanguageId(Long userId, String languageId);


}
