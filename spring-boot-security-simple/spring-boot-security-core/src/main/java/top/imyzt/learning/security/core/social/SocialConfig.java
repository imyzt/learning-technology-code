package top.imyzt.learning.security.core.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;
import top.imyzt.learning.security.core.properties.SecurityProperties;

import javax.sql.DataSource;

/**
 * @author imyzt
 * @date 2019/6/17
 * @description 社交登录配置
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    private final DataSource dataSource;
    private final SecurityProperties securityProperties;


    private final ConnectionSignUp connectionSignUp;

    public SocialConfig(DataSource dataSource,
                        SecurityProperties securityProperties,
                        // 并不是所有子系统都会配置自动创建用户
                        @Autowired(required = false) ConnectionSignUp connectionSignUp) {
        this.dataSource = dataSource;
        this.securityProperties = securityProperties;
        this.connectionSignUp = connectionSignUp;
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(
                // 数据源
                dataSource,
                // 从多个ConnectionFactory中查找对应的Factory
                connectionFactoryLocator,
                // 对数据库数据进行加解密, 这里没有做加解密
                Encryptors.noOpText());

        // 设置表前缀
        //jdbcUsersConnectionRepository.setTablePrefix("");

        // 当子应用配置了自动注册策略时,将其注入
        if (null != connectionSignUp) {
            jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
        }
        return jdbcUsersConnectionRepository;
    }

    /**
     * 使用自己实现的 DefaultSpringSocialConfigurer 替换掉 原始的 SpringSocialConfigurer
      */
    @Bean
    public SpringSocialConfigurer springSocialConfigurer() {
        DefaultSpringSocialConfigurer configurer =
                new DefaultSpringSocialConfigurer(securityProperties.getSocial().getFilterProcessUrl());
        // 告诉socialFilter, 注册页面是我们自己配置的页面
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return configurer;

    }

    /**
     * 工具类
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator)) {};
    }
}
