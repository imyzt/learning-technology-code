package top.imyzt.learning.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.imyzt.learning.security.core.properties.SecurityProperties;
import top.imyzt.learning.security.core.validate.code.image.ImageCodeGenerator;
import top.imyzt.learning.security.core.validate.code.sms.DefaultSmsCodeSender;
import top.imyzt.learning.security.core.validate.code.sms.SmsCodeSender;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 验证码配置
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 图片验证码默认使用 {@link ImageCodeGenerator} 实现, 当存在(即子应用自己创建)名为"imageCodeGenerator"的bean时, 不使用这个实现
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }

    /**
     * 短信发送默认使用 {@link DefaultSmsCodeSender} 实现
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }
}
