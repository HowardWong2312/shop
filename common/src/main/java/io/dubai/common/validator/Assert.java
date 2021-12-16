package io.dubai.common.validator;

import io.dubai.common.exception.RRException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @author mother fucker
 */
public abstract class Assert {

    public static void isBlank(String str, String msg) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(msg);
        }
    }

    public static void isBlank(String str, Integer code,String msg) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(msg, code);
        }
    }

    public static void isNull(Object object, Integer code,String msg) {
        if (object == null) {
            throw new RRException(msg, code);
        }
    }
}
