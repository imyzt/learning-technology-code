package top.imyzt.agent.proxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author imyzt
 * @date 2024/08/11
 * @description 描述信息
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    String prefix();
}
