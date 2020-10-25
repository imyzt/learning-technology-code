package top.imyzt.learning.spring.framework.annotations;

import java.lang.annotation.*;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description RequestMapping
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String value() default "";
}
