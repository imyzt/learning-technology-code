package top.imyzt.learning.security.demo.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/6/4
 * @description TimeInterceptor
 */
@Component
@Slf4j
public class TimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        log.info("preHandle");

        log.info("处理器类名: {}", ((HandlerMethod) handler).getBean().getClass().getName());
        log.info("处理器方法名: {}", ((HandlerMethod) handler).getMethod().getName());

        request.setAttribute("startTime", LocalDateTime.now());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
        LocalDateTime startTime = (LocalDateTime) httpServletRequest.getAttribute("startTime");
        long between = Duration.between(startTime, LocalDateTime.now()).toMillis();
        log.info("time interceptor postHandle 耗时: {} 毫秒", between);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
        LocalDateTime startTime = (LocalDateTime) httpServletRequest.getAttribute("startTime");
        long between = Duration.between(startTime, LocalDateTime.now()).toMillis();
        log.info("time interceptor afterCompletion 耗时: {} 毫秒", between);
        if (null != e) {
            log.error("afterCompletion ex", e);
        }
    }
}
