package io.dubai.common.easemob.comm;

import io.dubai.common.sys.service.SysConfigService;
import io.dubai.common.utils.SpringContextUtils;
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

    private static SysConfigService sysConfigService;

    static {
        OrgInfo.sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");
        ORG_NAME = sysConfigService.getValue("ORG_NAME");
        APP_NAME_USER = sysConfigService.getValue("APP_NAME_USER");
//        APP_NAME_DOC = prop.getProperty("APP_NAME_DOC");
    }
}
