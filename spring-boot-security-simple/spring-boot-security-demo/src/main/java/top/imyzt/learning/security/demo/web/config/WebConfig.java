package top.imyzt.learning.security.demo.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.imyzt.learning.security.demo.web.filter.TimeFilter;
import top.imyzt.learning.security.demo.web.interceptor.TimeInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description WebConfig
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Autowired
    private TimeInterceptor timeInterceptor;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter timeFilter = new TimeFilter();

        registrationBean.setFilter(timeFilter);
        List<String> urlPatterns = Collections.singletonList("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }

    /**
     * 配置异步拦截器.
     */
    /*@Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {

    }*/

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }
}
