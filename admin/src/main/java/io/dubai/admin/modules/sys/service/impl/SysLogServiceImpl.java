package io.dubai.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.sys.dao.SysLogDao;
import io.dubai.admin.modules.sys.entity.SysLogEntity;
import io.dubai.admin.modules.sys.service.SysLogService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");

        IPage<SysLogEntity> page = this.page(
                new Query<SysLogEntity>().getPage(params),
                new QueryWrapper<SysLogEntity>()
                        .like(StringUtils.isNotBlank(key), "username", key)
                        .or().like(StringUtils.isNotBlank(key), "operation", key)
                        .orderByDesc("create_date")
        );

        return new PageUtils(page);
    }
}
