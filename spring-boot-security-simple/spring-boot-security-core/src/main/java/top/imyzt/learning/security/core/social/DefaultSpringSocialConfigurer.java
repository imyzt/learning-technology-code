package top.imyzt.learning.security.core.social;


import lombok.AllArgsConstructor;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 自定义社交登录过滤器, 配置指定前缀
 */
@AllArgsConstructor
public class DefaultSpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter socialAuthenticationFilter = (SocialAuthenticationFilter) super.postProcess(object);
        socialAuthenticationFilter.setFilterProcessesUrl(filterProcessesUrl);
        return (T) socialAuthenticationFilter;
    }
}
