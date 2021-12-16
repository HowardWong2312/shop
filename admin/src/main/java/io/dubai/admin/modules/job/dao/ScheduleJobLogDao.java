package io.dubai.admin.modules.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.dubai.admin.modules.job.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author mother fucker
 */
@Mapper
public interface ScheduleJobLogDao extends BaseMapper<ScheduleJobLogEntity> {

}
