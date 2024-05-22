package top.imyzt.learning.spring.startup.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.imyzt.learning.spring.startup.core.TestABean;
import top.imyzt.learning.spring.startup.core.TestBBean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2024/05/21
 * @description 配置类
 */
@Configuration
public class RegisterConfiguration {

    @Bean(bootstrap = Bean.Bootstrap.BACKGROUND)
    public TestABean testABean() {
        return new TestABean();
    }

    @Bean(bootstrap = Bean.Bootstrap.BACKGROUND)
    public TestBBean testBBean() {
        return new TestBBean();
    }

    @Bean()
    public Executor bootstrapExecutor() {
        return new ThreadPoolExecutor(2, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1024));
    }
}
