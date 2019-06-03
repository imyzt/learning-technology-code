package top.imyzt.learning.security.demo.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.imyzt.learning.security.demo.web.filter.TimeFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description WebConfig
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter timeFilter = new TimeFilter();

        registrationBean.setFilter(timeFilter);
        List<String> urlPatterns = Collections.singletonList("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
