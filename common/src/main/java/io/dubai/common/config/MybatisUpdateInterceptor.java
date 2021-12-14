//package io.dubai.common.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.plugin.*;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.util.Properties;
//
//@Slf4j
//@Component
//@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
//public class MybatisUpdateInterceptor implements Interceptor {
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//        String sqlId = mappedStatement.getId();
//        log.debug("------sqlId------" + sqlId);
//        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
//        Object parameter = invocation.getArgs()[1];
//        log.debug("------sqlCommandType------" + sqlCommandType);
//
//        if (parameter == null) {
//            return invocation.proceed();
//        }
//        if (SqlCommandType.INSERT == sqlCommandType || SqlCommandType.UPDATE == sqlCommandType) {
//            Field[] fields = parameter.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                if (field.getType().equals(String.class)) {
//                    field.setAccessible(true);
//                    Object o = field.get(parameter);
//                    field.setAccessible(false);
//                    String newVal = o == null ? "" : String.valueOf(o).trim();
//                    field.setAccessible(true);
//                    field.set(parameter, newVal);
//                    field.setAccessible(false);
//                }
//            }
//        }
//
//        return invocation.proceed();
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//        // TODO Auto-generated method stub
//    }
//
//}
