package io.dubai.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dubai.admin.modules.sys.dao.SysDictDao;
import io.dubai.admin.modules.sys.entity.SysDictEntity;
import io.dubai.admin.modules.sys.service.SysDictService;
import io.dubai.common.utils.PageUtils;
import io.dubai.common.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String) params.get("name");

        IPage<SysDictEntity> page = this.page(
                new Query<SysDictEntity>().getPage(params),
                new QueryWrapper<SysDictEntity>()
                        .like(StringUtils.isNotBlank(name), "name", name)
        );

        return new PageUtils(page);
    }

    @Override
    public List<SysDictEntity> queryByType(String type) {
        List<SysDictEntity> list = this.list(new QueryWrapper<SysDictEntity>().eq("type", type));
        return list;
    }

    @Override
    public List<SysDictEntity> queryByTypeAndCode(String type, String code) {
        List<SysDictEntity> list = this.list(new QueryWrapper<SysDictEntity>().eq("type", type).eq("code", code));
        return list;
    }

}
