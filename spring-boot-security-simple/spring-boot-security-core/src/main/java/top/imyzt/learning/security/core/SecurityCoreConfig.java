package top.imyzt.learning.security.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.imyzt.learning.security.core.properties.SecurityProperties;

/**
 * @author imyzt
 * @date 2019/6/6
 * @description 安全配置
 */
@Configuration
@EnableConfigurationProperties(value = SecurityProperties.class)
public class SecurityCoreConfig {
}
