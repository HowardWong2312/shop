package io.dubai.common.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dubai.common.sys.entity.SysConfig;
import io.dubai.common.utils.PageUtils;

import java.util.Map;

/**
 * 系统配置信息
 *
 * @author mother fucker
 */
public interface SysConfigService extends IService<SysConfig> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存配置信息
     */
    void saveConfig(SysConfig config);

    /**
     * 更新配置信息
     */
    void update(SysConfig config);

    /**
     * 根据key，更新value
     */
    void updateValueByKey(String key, String value);

    /**
     * 删除配置信息
     */
    void deleteBatch(Long[] ids);

    /**
     * 根据key，获取配置的value值
     *
     * @param key key
     */
    String getValue(String key);

    /**
     * 根据key，获取value的Object对象
     *
     * @param key   key
     * @param clazz Object对象
     */
    <T> T getConfigObject(String key, Class<T> clazz);

}
