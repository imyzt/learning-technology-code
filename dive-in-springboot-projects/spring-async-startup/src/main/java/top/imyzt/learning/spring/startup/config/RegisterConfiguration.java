package top.imyzt.learning.spring.startup.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.imyzt.learning.spring.startup.core.TestABean;
import top.imyzt.learning.spring.startup.core.TestBBean;

/**
 * @author imyzt
 * @date 2024/05/21
 * @description 配置类
 */
@Configuration
public class RegisterConfiguration {

    @Bean
    public TestABean testABean() {
        return new TestABean();
    }

    @Bean
    public TestBBean testBBean() {
        return new TestBBean();
    }

}
