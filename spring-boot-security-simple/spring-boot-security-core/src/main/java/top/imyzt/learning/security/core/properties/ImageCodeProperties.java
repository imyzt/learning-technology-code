package top.imyzt.learning.security.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 图形验证码基本参数配置信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCodeProperties extends SmsCodeProperties{

    public ImageCodeProperties() {
        // 短信验证码默认长度6, 图形验证码默认长度4
        setLength(4);
    }

    private int width = 67;
    private int height = 23;

}
