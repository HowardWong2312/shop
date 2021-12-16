package io.dubai.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.sys.entity.SysLogEntity;
import io.dubai.common.utils.PageUtils;

import java.util.Map;


/**
 * 系统日志
 *
 * @author mother fucker
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
