package top.imyzt.learning.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.imyzt.learning.security.core.properties.SecurityProperties;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description ValidateCodeConfig
 */
@Configuration
public class ValidateCodeConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 图片验证码默认使用 {@link ImageCodeGenerator} 实现, 当存在(即子应用自己创建)名为"imageCodeGenerator"的bean时, 不使用这个实现
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }
}
