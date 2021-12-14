package io.dubai.modules.other.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import io.dubai.modules.other.dao.BannerDao;
import io.dubai.modules.other.dao.PaymentDao;
import io.dubai.modules.other.entity.Banner;
import io.dubai.modules.other.entity.Payment;
import io.dubai.modules.other.service.BannerService;
import io.dubai.modules.other.service.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("bannerService")
public class BannerServiceImpl extends ServiceImpl<BannerDao, Banner> implements BannerService {


}
