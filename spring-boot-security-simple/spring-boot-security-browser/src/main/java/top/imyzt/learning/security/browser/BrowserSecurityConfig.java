package top.imyzt.learning.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;
import top.imyzt.learning.security.core.authentication.AbstractChannelSecurityConfig;
import top.imyzt.learning.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import top.imyzt.learning.security.core.properties.SecurityConstants;
import top.imyzt.learning.security.core.properties.SecurityProperties;
import top.imyzt.learning.security.core.validate.code.ValidateCodeSecurityConfig;

import javax.sql.DataSource;

/**
 * @author imyzt
 * @date 2019/6/5
 * @description Spring Security 网络安全配置类
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    private final SecurityProperties securityProperties;

    private final DataSource dataSource;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    public BrowserSecurityConfig(SecurityProperties securityProperties,
                                 DataSource dataSource,
                                 SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig) {
        this.securityProperties = securityProperties;
        this.dataSource = dataSource;
        this.smsCodeAuthenticationSecurityConfig = smsCodeAuthenticationSecurityConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        smsCodeAuthenticationSecurityConfig.setUserDetailServiceImpl(userDetailsServiceImpl);

        applyPasswordAuthenticationConfig(http);

        http
                .apply(validateCodeSecurityConfig)
                    .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                    .and()
                // 社交登录配置
                .apply(springSocialConfigurer)
                    .and()
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsServiceImpl)
                    .and()
                .authorizeRequests()
                    .antMatchers(
                            SecurityConstants.DEFAULT_UN_AUTHENTICATION_URL,
                            SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                            securityProperties.getBrowser().getLoginPage(),
                            SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                            securityProperties.getBrowser().getSignUpUrl(),
                            "/user/register"
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .csrf()
                    .disable();


        /*http
                // 将自定义的短信验证码过滤器 注册在 用户名密码过滤器前面
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 将自定义的验证码过滤器 注册在 用户名密码过滤器前面
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 表单登录配置
                .formLogin()
                    // 未登录时跳转的页面/请求
                    .loginPage("/authentication/require")
                    // 注册登录成功的处理器
                    .successHandler(successHandler)
                    // 注册登录失败的处理器
                    .failureHandler(failureHandler)
                    // 将此请求转发给security#login方法处理
                    .loginProcessingUrl("/authentication/form")
                    .and()
                // 记住我配置
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsServiceImpl)
                    .and()
                // 授权配置
                .authorizeRequests()
                    // 不验证以下请求
                    .antMatchers("/authentication/require",
                            securityProperties.getBrowser().getLoginPage(),
                            "/code/*").permitAll()
                    // 所有请求
                    .anyRequest()
                    // 需要授权
                    .authenticated()
                    .and()
                // 关闭跨站请求伪造
                .csrf()
                    .disable()
                // 添加配置
                .apply(smsCodeAuthenticationSecurityConfig);*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 启动时建表. 初次运行时开启
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
