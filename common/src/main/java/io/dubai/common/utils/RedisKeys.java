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


    public static String getSysConfigKey(String key) {
        return "sys:config:" + key;
    }

}
