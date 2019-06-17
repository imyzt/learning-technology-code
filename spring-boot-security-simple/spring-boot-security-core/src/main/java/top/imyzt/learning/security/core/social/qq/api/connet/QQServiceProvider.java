package top.imyzt.learning.security.core.social.qq.api.connet;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;
import top.imyzt.learning.security.core.social.qq.api.QQ;
import top.imyzt.learning.security.core.social.qq.api.QQImpl;

/**
 * @author imyzt
 * @date 2019/6/17
 * @description QQ 服务提供者
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;
    /**
     * 引导用户授权的地址
     */
    private static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 获取令牌访问的地址
     */
    private static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId, String appSecret) {
        super(new OAuth2Template(appId, appSecret, AUTHORIZE_URL, ACCESS_TOKEN_URL));
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}
