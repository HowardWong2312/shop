package io.dubai.common.annotation;

import java.lang.annotation.*;

/**
 * 登录效验
 *
 * @author mother fucker
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
