package top.imyzt.learning.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author imyzt
 * @date 2021/01/02
 * @description 是否是MacOS
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnOSCondition.class)
public @interface ConditionOnOS {

    /**
     * 系统类型
     */
    OSType osType();

    public enum OSType {
        MAC,
        LINUX,
        WINDOWS,

        ;
    }
}