package top.imyzt.learning.security.demo.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description TimeFilter
 */
@Slf4j
public class TimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info("timeFilter start");
        LocalDateTime start = LocalDateTime.now();

        filterChain.doFilter(servletRequest, servletResponse);

        long between = Duration.between(start, LocalDateTime.now()).toMillis();
        log.info("time filter 耗时: {} 毫秒", between);

        log.info("timeFilter finish");
    }

    @Override
    public void destroy() {
        log.info("time filter destroy");
    }
}
