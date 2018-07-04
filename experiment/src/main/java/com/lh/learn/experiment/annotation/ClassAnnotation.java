package com.lh.learn.experiment.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义类、接口、枚举注解
 *
 * @author liuhai on 2018/2/24.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface ClassAnnotation {
    String value() default "default";
}
