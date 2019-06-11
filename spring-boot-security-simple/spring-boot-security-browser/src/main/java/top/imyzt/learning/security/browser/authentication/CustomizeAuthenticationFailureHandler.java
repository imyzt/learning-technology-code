package top.imyzt.learning.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.imyzt.learning.security.browser.support.SimpleResponse;
import top.imyzt.learning.security.core.properties.LogType;
import top.imyzt.learning.security.core.properties.SecurityProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/6/6
 * @description 登录失败的处理器
 */
@Component
@Slf4j
public class CustomizeAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    private final SecurityProperties securityProperties;

    public CustomizeAuthenticationFailureHandler(ObjectMapper objectMapper,
                                                 SecurityProperties securityProperties) {
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        log.info("用户登录失败");

        // 异步请求返回错误信息
        if (LogType.JSON.equals(securityProperties.getBrowser().getLogType())) {

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            // 只返回错误的消息
            response.getWriter().write(objectMapper.writeValueAsString(SimpleResponse.of(exception.getMessage())));
        } else {
            // 网页请求直接返回Spring错误页
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
