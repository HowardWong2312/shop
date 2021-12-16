package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserInfoDao;
import io.dubai.admin.modules.user.entity.UserInfo;
import io.dubai.admin.modules.user.service.UserInfoService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.common.utils.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserInfo> page = new Query<UserInfo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

    @Override
    public UserInfo insert(UserInfo userInfo) {
        baseMapper.insert(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        baseMapper.updateById(userInfo);
        Object temp = redisTemplate.opsForValue().get(RedisKeys.userInfoKey+userInfo.getUserId());
        if(temp != null){
            redisTemplate.opsForValue().set(RedisKeys.userInfoKey+userInfo.getUserId(),userInfo);
        }
        return userInfo;
    }

}
