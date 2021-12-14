package io.dubai.config;

import io.dubai.dao.*;
import io.dubai.utils.RRException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * 数据库配置
 *
 * @author howard
 */
@Configuration
public class DbConfig {
    @Value("${dubai.database: mysql}")
    private String database;
    @Resource
    private MySQLGeneratorDao mySQLGeneratorDao;
    @Resource
    private OracleGeneratorDao oracleGeneratorDao;
    @Resource
    private SQLServerGeneratorDao sqlServerGeneratorDao;
    @Resource
    private PostgreSQLGeneratorDao postgreSQLGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao(){
        if("mysql".equalsIgnoreCase(database)){
            return mySQLGeneratorDao;
        }else if("oracle".equalsIgnoreCase(database)){
            return oracleGeneratorDao;
        }else if("sqlserver".equalsIgnoreCase(database)){
            return sqlServerGeneratorDao;
        }else if("postgresql".equalsIgnoreCase(database)){
            return postgreSQLGeneratorDao;
        }else {
            throw new RRException("不支持当前数据库：" + database);
        }
    }
}
