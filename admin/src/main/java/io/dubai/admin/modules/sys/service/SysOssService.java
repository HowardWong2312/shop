package io.dubai.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.sys.entity.SysOssEntity;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 文件上传
 *
 * @author mother fucker
 */
public interface SysOssService extends IService<SysOssEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
