package io.dubai.common.easemob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by easemob on 2017/3/31.
 */
public class OrgInfo {

    public static String ORG_NAME;
    public static String APP_NAME_USER;
//    public static String APP_NAME_DOC;
    public static final Logger logger = LoggerFactory.getLogger(OrgInfo.class);

    static {
        InputStream inputStream = OrgInfo.class.getClassLoader().getResourceAsStream("config.properties");
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        ORG_NAME = prop.getProperty("ORG_NAME");
        APP_NAME_USER = prop.getProperty("APP_NAME_USER");
//        APP_NAME_DOC = prop.getProperty("APP_NAME_DOC");
    }
}
