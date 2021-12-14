package io.dubai.modules.user.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.dubai.modules.user.dao.UserAddressDao;
import io.dubai.modules.user.entity.UserAddress;
import io.dubai.modules.user.service.UserAddressService;


@Service("userAddressService")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressDao, UserAddress> implements UserAddressService {


}
