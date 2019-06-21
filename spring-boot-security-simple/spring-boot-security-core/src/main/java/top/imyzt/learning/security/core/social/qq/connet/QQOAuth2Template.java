package top.imyzt.learning.security.core.social.qq.connet;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author imyzt
 * @date 2019/06/21
 * @description
 */
@Slf4j
public class QQOAuth2Template extends OAuth2Template {

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);

        /**
         * 只有设置为true时, 发请求时才会带上client_id和client_secret.
         * {@link    public AccessGrant exchangeForAccess() 134行 }
         */
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 处理获取AccessToken时,QQ服务商返回的内容
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {

        String result = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);

        log.info("获取accessToken的响应内容: {}", result);

        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(result, "&");

        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");

        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }

    /**
     * QQ服务商返回的内容为TEXT/HTML格式,默认的RestTemplate没有该格式的处理转换器, 增加一个转换器
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate;
    }
}
