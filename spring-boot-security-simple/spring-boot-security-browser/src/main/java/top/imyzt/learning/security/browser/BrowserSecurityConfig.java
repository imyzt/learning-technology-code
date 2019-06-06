package top.imyzt.learning.security.browser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.imyzt.learning.security.browser.authentication.CustomizeAuthenticationFailureHandler;
import top.imyzt.learning.security.browser.authentication.CustomizeAuthenticationSuccessHandler;
import top.imyzt.learning.security.core.properties.SecurityProperties;

/**
 * @author imyzt
 * @date 2019/6/5
 * @description Spring Security 网络安全配置类
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProperties securityProperties;

    private final CustomizeAuthenticationSuccessHandler successHandler;

    private final CustomizeAuthenticationFailureHandler failureHandler;

    public BrowserSecurityConfig(SecurityProperties securityProperties,
                                 CustomizeAuthenticationSuccessHandler successHandler,
                                 CustomizeAuthenticationFailureHandler failureHandler) {
        this.securityProperties = securityProperties;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // basic登录`
        //http.httpBasic()

        // 表单认证
        http.formLogin()
                // 未登录时跳转的页面/请求
                .loginPage("/authentication/require")
                // 注册登录成功的处理器
                .successHandler(successHandler)
                // 注册登录失败的处理器
                .failureHandler(failureHandler)
                // 将此请求转发给security#login方法处理
                .loginProcessingUrl("/authentication/form")
                .and()
                // 开始授权配置
                .authorizeRequests()
                // 不验证以下请求
                .antMatchers("/authentication/require", securityProperties.getBrowser().getLoginPage()).permitAll()
                // 所有请求
                .anyRequest()
                // 需要授权
                .authenticated()
                .and()
                // 关闭跨站请求伪造
                .csrf().disable();
    }
}
