package io.dubai.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.admin.modules.sys.entity.SysDictEntity;
import io.dubai.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author howard
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SysDictEntity> queryByType(String type);

    List<SysDictEntity> queryByTypeAndCode(String type, String code);
}

