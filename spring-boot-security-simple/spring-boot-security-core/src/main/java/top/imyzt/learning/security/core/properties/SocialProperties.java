package top.imyzt.learning.security.core.properties;

import lombok.Data;

@Data
public class SocialProperties {

    /**
     * 社交登录过滤器拦截指定前缀
     */
    private String filterProcessUrl;

    private QQProperties qq = new QQProperties();
}
