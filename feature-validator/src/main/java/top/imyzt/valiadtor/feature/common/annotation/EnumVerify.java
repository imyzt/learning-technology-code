package top.imyzt.valiadtor.feature.common.annotation;

import top.imyzt.valiadtor.feature.web.validator.EnumCode;
import top.imyzt.valiadtor.feature.web.validator.EnumVerifyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author imyzt
 * @date 2019/07/12
 * @description 枚举类型校验器
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumVerifyValidator.class)
public @interface EnumVerify {

    /**
     * 错误提示消息
     */
    String message();

    /**
     * 枚举类
     */
    Class<? extends EnumCode> enumClass();

    /**
     * 为空时是否校验
     * 默认为空不校验
     */
    boolean unVerifyNull() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
