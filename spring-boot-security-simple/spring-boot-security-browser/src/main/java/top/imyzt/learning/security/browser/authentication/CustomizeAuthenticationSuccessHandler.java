package top.imyzt.learning.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.imyzt.learning.security.core.properties.LogType;
import top.imyzt.learning.security.core.properties.SecurityProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/6/6
 * @description 登录成功的处理器
 */
@Component
@Slf4j
public class CustomizeAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final SecurityProperties securityProperties;

    public CustomizeAuthenticationSuccessHandler(ObjectMapper objectMapper,
                                                 SecurityProperties securityProperties) {
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        log.info("登录成功, username = {}", userDetails.getUsername());

        // 异步请求返回错误信息
        if (LogType.JSON.equals(securityProperties.getBrowser().getLogType())) {

            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else {
            // 网页请求直接返回Spring错误页
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}