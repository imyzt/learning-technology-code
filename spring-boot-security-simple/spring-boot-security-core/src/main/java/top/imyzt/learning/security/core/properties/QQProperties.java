package top.imyzt.learning.security.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @author imyzt
 * @date 2019/6/17
 * @description QQ登录配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QQProperties extends SocialProperties {

    private String providerId = "qq";

}
