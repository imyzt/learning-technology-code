package top.imyzt.learning.security.core.validate.code;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import top.imyzt.learning.security.core.properties.SecurityConstants;
import top.imyzt.learning.security.core.properties.SecurityProperties;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author imyzt
 * @date 2019/6/10
 * @description 图形验证码验证过滤器. 继承OncePerRequestFilter保证只会被执行一次
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private final SecurityProperties securityProperties;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 验证码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;

    public ValidateCodeFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        // 表单登录需要图片验证码校验的URL
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        addUrl2Map(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

        // 手机号登录需要短信验证码校验的URL
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrl2Map(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
    }

    /**
     * 将系统中配置的需要校验验证码的URL根据校验类型放入map中
     * @param urlStr urls
     * @param type 验证码类型
     */
    protected void addUrl2Map(String urlStr, ValidateCodeType type) {
        if (StringUtils.isNotBlank(urlStr)) {
            String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlStr, ",");
            for (String url : configUrls) {
                urlMap.put(url, type);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

            ValidateCodeType validateCodeType = getValidateCodeType(request);

        // 请求需要校验
        if (null != validateCodeType) {
            log.info("校验请求 = {} 中的验证码, 验证码类型 = {}", request.getRequestURI(), validateCodeType);
            try {
                validateCodeProcessorHolder
                        .findValidateCodeProcessor(validateCodeType)
                        .validate(new ServletWebRequest(request, response));
                log.info("{}验证码校验通过", validateCodeType.toString());
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 系统初始化时, 已为每个需要校验的请求设置了类型(短信/图片验证码)
     * 获取校验码类型,当前请求不需要校验时返回null
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        AtomicReference<ValidateCodeType> validateCodeType = new AtomicReference<>(null);
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), HttpMethod.GET.name())) {
            urlMap.keySet().forEach(url -> {
                if (pathMatcher.match(url, request.getRequestURI())){
                    validateCodeType.set(urlMap.get(url));
                }
            });
        }
        return validateCodeType.get();
    }

}
