package top.imyzt.learning.security.core.validate.code;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import top.imyzt.learning.security.core.properties.SecurityProperties;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author imyzt
 * @date 2019/6/10
 * @description 短信验证码验证过滤器. 继承OncePerRequestFilter保证只会被执行一次
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 短信验证码配置需过滤的URL
     */
    private Set<String> pathPatterns = new HashSet<>();

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private final SecurityProperties securityProperties;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;

    public SmsCodeFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(
                securityProperties.getCode().getSms().getUrl(), ",");
        // 所有子系统定义的需要过滤的接口
        CollectionUtils.addIgnoreNull(pathPatterns, configUrls);
        // 默认登录接口, 必须验证
        pathPatterns.add("/authentication/mobile");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        boolean action = false;
        for (String pathPattern : pathPatterns) {
            // 如果请求的URI和配置的需要过滤的接口匹配上, 设置action=true
            if (pathMatcher.match(pathPattern, request.getRequestURI())) {
                action = true;
                break;
            }
        }

        if (action) {
            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                // 异常时, 调用自定义的异常处理器将异常返回前台页面或者返回错误json信息
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                // 验证失败不继续向下进行
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 校验请求中验证码
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {

        String sessionKey = ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS";

        ValidateCode codeInSession = (ValidateCode) sessionStrategy.getAttribute(request, sessionKey);

        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");


        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }
}
