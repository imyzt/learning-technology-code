package top.imyzt.learning.security.core.validate.code.sms;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.imyzt.learning.security.core.properties.SecurityProperties;
import top.imyzt.learning.security.core.validate.code.ValidateCode;
import top.imyzt.learning.security.core.validate.code.ValidateCodeGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 短信验证码生成默认实现
 */
@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(HttpServletRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
