/**
 *
 */

package io.dubai.common.utils;


/**
 * Redis所有Keys
 *
 * @author mother fucker
 */
public class RedisKeys {

    public static final String userInfoKey = "userInfo_";

    public static final String userOnlineKey = "userOnline";

    public static final String citiesKey = "cities";

    public static final String smsCodeKey = "sms:";

    public static final String sysConfigKey = "sys:config:";

    public static String getSmsCodeKey(String key) {
        return smsCodeKey + key;
    }

    public static String getSysConfigKey(String key) {
        return sysConfigKey + key;
    }

}
