package top.imyzt.learning.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author imyzt
 * @date 2019/6/13
 * @description 校验码处理器提供者对象
 */
@Component
public class ValidateCodeProcessorHolder {

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.toString().toUpperCase());
    }

    /**
     * 获取校验码处理器
     * @param type 验证码类型
     * @return 验证码处理器
     */
    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor validateCodeProcessor = validateCodeProcessors.get(name);
        if (null == validateCodeProcessor) {
            throw new ValidateCodeException("验证码处理器 " + name + " 不存在");
        }
        return validateCodeProcessor;
    }

}
