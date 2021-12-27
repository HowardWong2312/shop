package io.dubai.admin.modules.other.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dubai.admin.modules.other.entity.ShowVideo;
import io.dubai.admin.modules.user.entity.vo.UserWithdrawVo;
import io.dubai.common.utils.PageUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 我的视频
 *
 * @author mother fucker
 * @email admin@gmail.com
 * @date 2021-12-25 19:13:36
 */
@Mapper
public interface ShowVideoDao extends BaseMapper<ShowVideo> {

    List<ShowVideo> queryPage(IPage page, Map<String, Object> params);

}
