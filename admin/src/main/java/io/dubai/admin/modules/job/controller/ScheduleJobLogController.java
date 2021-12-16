package io.dubai.admin.modules.job.controller;

import io.dubai.admin.modules.job.entity.ScheduleJobLogEntity;
import io.dubai.admin.modules.job.service.ScheduleJobLogService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 定时任务日志
 *
 * @author mother fucker
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
    @Resource
    private ScheduleJobLogService scheduleJobLogService;

    /**
     * 定时任务日志列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:schedule:log")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = scheduleJobLogService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 定时任务日志信息
     */
    @RequestMapping("/info/{logId}")
    public R info(@PathVariable("logId") Long logId) {
        ScheduleJobLogEntity log = scheduleJobLogService.getById(logId);

        return R.ok().put("log", log);
    }
}
