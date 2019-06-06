package top.imyzt.learning.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author imyzt
 * @date 2019/6/6
 * @description 安全组件核心配置文件
 */
@ConfigurationProperties("system.security")
@Data
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();
}
