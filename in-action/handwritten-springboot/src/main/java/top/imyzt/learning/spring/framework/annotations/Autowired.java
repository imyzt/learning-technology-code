package top.imyzt.learning.spring.framework.annotations;

import java.lang.annotation.*;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description Autowired
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    String value() default "";
}
