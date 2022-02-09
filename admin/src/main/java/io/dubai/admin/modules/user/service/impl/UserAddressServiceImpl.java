package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserAddressDao;
import io.dubai.admin.modules.user.entity.UserAddress;
import io.dubai.admin.modules.user.entity.vo.UserAddressVo;
import io.dubai.admin.modules.user.service.UserAddressService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userAddressService")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressDao, UserAddress> implements UserAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserAddressVo> page = new Query<UserAddressVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

}
