package io.dubai.admin.modules.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.job.dao.ScheduleJobLogDao;
import io.dubai.admin.modules.job.entity.ScheduleJobLogEntity;
import io.dubai.admin.modules.job.service.ScheduleJobLogService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String jobId = (String) params.get("jobId");

        IPage<ScheduleJobLogEntity> page = this.page(
                new Query<ScheduleJobLogEntity>().getPage(params),
                new QueryWrapper<ScheduleJobLogEntity>()
                        .eq(StringUtils.isNotBlank(jobId), "job_id", jobId)
                        .orderByDesc("create_time")
        );

        return new PageUtils(page);
    }

}
