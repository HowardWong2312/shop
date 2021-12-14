/**
 *
 */

package io.dubai.common.utils;


/**
 * Redis所有Keys
 *
 * @author howard
 */
public class RedisKeys {

    public static final String userInfoKey = "userInfo_";


    public static String getSysConfigKey(String key) {
        return "sys:config:" + key;
    }

}
