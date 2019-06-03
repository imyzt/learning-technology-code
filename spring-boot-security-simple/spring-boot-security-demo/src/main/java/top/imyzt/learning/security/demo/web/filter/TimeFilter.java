package top.imyzt.learning.security.demo.web.filter;

import javax.servlet.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description TimeFilter
 */
//@Component
public class TimeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("timeFilter start");
        LocalDateTime start = LocalDateTime.now();

        filterChain.doFilter(servletRequest, servletResponse);

        long between = Duration.between(start, LocalDateTime.now()).toMillis();
        System.out.println("time filter 耗时: " + between + " 毫秒");

        System.out.println("timeFilter finish");
    }

    @Override
    public void destroy() {
        System.out.println("time filter destroy");
    }
}
