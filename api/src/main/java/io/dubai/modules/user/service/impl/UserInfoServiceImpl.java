package io.dubai.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cz.czUser.system.entity.UserInfo;
import io.dubai.common.utils.RedisKeys;
import io.dubai.modules.user.dao.UserInfoDao;
import io.dubai.modules.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserInfo queryByUserId(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public UserInfo insert(UserInfo userInfo) {
        baseMapper.insert(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        baseMapper.updateById(userInfo);
        Object temp = redisTemplate.opsForValue().get(RedisKeys.userInfoKey + userInfo.getUserId());
        if (temp != null) {
            redisTemplate.opsForValue().set(RedisKeys.userInfoKey + userInfo.getUserId(), userInfo);
        }
        return userInfo;
    }
}
