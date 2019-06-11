package top.imyzt.learning.security.core.properties;

import lombok.Data;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 图形验证码基本参数配置信息
 */
@Data
public class ImageCodeProperties {

    private int width = 67;
    private int height = 23;
    /**
     * 验证码默认长度
     */
    private int length = 4;
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
