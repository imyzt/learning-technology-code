package top.imyzt.learning.spring.framework.annotations;

import java.lang.annotation.*;

/**
 * @author imyzt
 * @date 2020/11/02
 * @description 响应结果json
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
}
