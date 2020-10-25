package top.imyzt.learning.spring.framework.annotations;

import java.lang.annotation.*;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description RestController
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestController {
}
