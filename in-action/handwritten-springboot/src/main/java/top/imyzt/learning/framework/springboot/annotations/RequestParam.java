package top.imyzt.learning.framework.springboot.annotations;

import java.lang.annotation.*;

/**
 * @author imyzt
 * @date 2020/10/25
 * @description RequestParam
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String name();

    String defaultValue() default "";

    boolean required() default false;
}
