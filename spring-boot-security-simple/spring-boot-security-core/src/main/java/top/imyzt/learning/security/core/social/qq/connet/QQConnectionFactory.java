package top.imyzt.learning.security.core.social.qq.connet;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import top.imyzt.learning.security.core.social.qq.api.QQ;

/**
 * @author imyzt
 * @date 2019/6/17
 * @description QQConnectionFactory
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
