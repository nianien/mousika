package com.skyfalling.mousika.define;

/**
 * @author liyifei <liyifei@kuaishou.com>
 * Created on 2021-11-05
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于声明某个对象为UDF
 *
 * @author liyifei
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Udf {

    /**
     * UDF名称,默认值{@link Class#getSimpleName()}的驼峰形式
     *
     * @return UDF别名
     */
    String value() default "";

}
