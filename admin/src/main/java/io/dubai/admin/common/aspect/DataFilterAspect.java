package io.dubai.admin.common.aspect;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dubai.admin.common.annotation.DataFilter;
import io.dubai.admin.modules.sys.entity.SysUserEntity;
import io.dubai.admin.modules.sys.service.SysDeptService;
import io.dubai.admin.modules.sys.service.SysRoleDeptService;
import io.dubai.admin.modules.sys.service.SysUserRoleService;
import io.dubai.admin.modules.sys.shiro.ShiroUtils;
import io.dubai.common.exception.RRException;
import io.dubai.common.utils.Constant;
import io.dubai.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据过滤，切面处理类
 *
 * @author howard
 */
@Aspect
@Component
public class DataFilterAspect {

    public Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SysRoleDeptService sysRoleDeptService;

    @Pointcut("@annotation(io.dubai.admin.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Pointcut("execution(public * io.dubai.modules.*.controller.*.list(..))")
    public void dataFilterCut2() {

    }

    @AfterReturning(value = "dataFilterCut2()", returning = "rvt")
    public void dataFilter2(JoinPoint point, Object rvt) {
        SysUserEntity user = ShiroUtils.getUserEntity();

        //如果不是超级管理员，则进行数据过滤
        if (user.getUserId() == Constant.SUPER_ADMIN) {
            return;
        }
        MethodSignature sig1 = (MethodSignature) point.getSignature();
        Method method1 = sig1.getMethod();
        if (null != rvt && null != method1.getDeclaringClass()) {
            try {
                if (rvt instanceof R) {
                    R r = (R) rvt;
                    if (r.containsKey("page")) {
                        JSONObject page = JSONObject.parseObject(JSON.toJSONStringWithDateFormat(r, "yyyy-MM-dd HH:mm:ss")).getJSONObject("page");
                        for (int i = 0; i < page.getJSONArray("list").size(); i++) {
                            page.getJSONArray("list").getJSONObject(i).put("phone", "*******");
                            page.getJSONArray("list").getJSONObject(i).put("idCard", "*******");
                            page.getJSONArray("list").getJSONObject(i).put("bankNumber", "*******");
                            page.getJSONArray("list").getJSONObject(i).put("bankRealName", "*******");
                            page.getJSONArray("list").getJSONObject(i).put("bankAddress", "*******");
                            page.getJSONArray("list").getJSONObject(i).put("regIp", "*******");
                            page.getJSONArray("list").getJSONObject(i).put("lastLoginIp", "*******");
                        }
                        r.put("page", page);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if (params != null && params instanceof Map) {
            SysUserEntity user = ShiroUtils.getUserEntity();

            //如果不是超级管理员，则进行数据过滤
            if (user.getUserId() != Constant.SUPER_ADMIN) {
                Map map = (Map) params;
                map.put(Constant.SQL_FILTER, getSQLFilter(user, point));
            }

            return;
        }

        throw new RRException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(SysUserEntity user, JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if (StringUtils.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }

        //部门ID列表
        Set<Long> deptIdList = new HashSet<>();

        //用户角色对应的部门ID列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        if (roleIdList.size() > 0) {
            List<Long> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
            deptIdList.addAll(userDeptIdList);
        }

        //用户子部门ID列表
        if (dataFilter.subDept()) {
            List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            deptIdList.addAll(subDeptIdList);
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        if (deptIdList.size() > 0) {
            sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
        }

        //没有本部门数据权限，也能查询本人数据
        if (dataFilter.user()) {
            if (deptIdList.size() > 0) {
                sqlFilter.append(" or ");
            }
            sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getUserId());
        }

        sqlFilter.append(")");

        if (sqlFilter.toString().trim().equals("()")) {
            return null;
        }

        return sqlFilter.toString();
    }
}
