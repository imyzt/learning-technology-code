package top.imyzt.learning.security.demo.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.imyzt.learning.security.demo.web.filter.TimeFilter;
import top.imyzt.learning.security.demo.web.interceptor.TimeInterceptor;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description WebConfig
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Resource
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


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/");
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
        argumentResolvers.add(pageableResolver);
        argumentResolvers.add(sortResolver);
    }
}
