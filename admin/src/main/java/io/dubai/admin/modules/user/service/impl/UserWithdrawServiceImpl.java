package io.dubai.admin.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.user.dao.UserWithdrawDao;
import io.dubai.admin.modules.user.entity.UserWithdraw;
import io.dubai.admin.modules.user.entity.vo.UserWithdrawVo;
import io.dubai.admin.modules.user.service.UserWithdrawService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("userWithdrawService")
public class UserWithdrawServiceImpl extends ServiceImpl<UserWithdrawDao, UserWithdraw> implements UserWithdrawService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserWithdrawVo> page = new Query<UserWithdrawVo>().getPage(params);
        page.setRecords(baseMapper.queryPage(page,params));
        return new PageUtils(page);
    }

    @Override
    public UserWithdrawVo queryById(Long id) {
        return baseMapper.queryById(id);
    }

}
