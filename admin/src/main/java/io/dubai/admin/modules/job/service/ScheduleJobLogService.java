package io.dubai.admin.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.job.entity.ScheduleJobLogEntity;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author howard
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
