package top.imyzt.learning.security.core.properties;

import lombok.Data;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 短信验证码基本参数配置信息
 */
@Data
public class SmsCodeProperties {

    /**
     * 验证码默认长度
     */
    private int length = 6;
    /**
     * 验证码默认过期时间60秒
     * Unit: 秒
     */
    private int expireIn = 60;

    /**
     * 配置过滤的URL, 多个使用逗号隔开
     */
    private String url;

}
