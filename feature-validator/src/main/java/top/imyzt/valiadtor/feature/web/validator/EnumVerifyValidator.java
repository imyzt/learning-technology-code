package top.imyzt.valiadtor.feature.web.validator;

import lombok.extern.slf4j.Slf4j;
import top.imyzt.valiadtor.feature.common.annotation.EnumVerify;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2019/07/12
 * @description 枚举验证实现
 */
@Slf4j
public class EnumVerifyValidator implements ConstraintValidator<EnumVerify, Integer> {

    private Class<? extends EnumCode> enumClass;
    private Boolean unVerifyNull;

    @Override
    public void initialize(EnumVerify enumVerify) {
        enumClass = enumVerify.enumClass();
        unVerifyNull = enumVerify.unVerifyNull();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {

        // 跳过校验
        if (skipValidator(value)) {
            return true;
        }

        EnumCode[] enumConstants = enumClass.getEnumConstants();
        for (EnumCode enumCode : enumConstants) {
            Integer code = enumCode.getCode();
            if (value.equals(code)) {
                return true;
            }
        }
        return false;
    }

    private boolean skipValidator(Integer value) {
        return Objects.isNull(value) && unVerifyNull;
    }
}
