package top.imyzt.learning.security.core.properties;

import lombok.Data;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 验证码配置, 包含图形验证码,短信验证码等
 */
@Data
public class ValidateCodeProperties {

    /**
     * 图形验证码配置
     */
    private ImageCodeProperties image = new ImageCodeProperties();

    /**
     * 短信验证码配置
     */
    private SmsCodeProperties sms = new SmsCodeProperties();
}
