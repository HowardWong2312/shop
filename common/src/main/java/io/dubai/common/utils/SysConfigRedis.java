package io.dubai.common.utils;


import io.dubai.common.sys.entity.SysConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 系统配置Redis
 *
 * @author mother fucker
 */
@Component
public class SysConfigRedis {
    @Resource
    private RedisUtils redisUtils;

    public void saveOrUpdate(SysConfig config) {
        if(config == null){
            return ;
        }
        String key = RedisKeys.getSysConfigKey(config.getParamKey());
        redisUtils.set(key, config);
    }

    public void delete(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        redisUtils.delete(key);
    }

    public SysConfig get(String configKey){
        String key = RedisKeys.getSysConfigKey(configKey);
        return redisUtils.get(key, SysConfig.class);
    }
}
