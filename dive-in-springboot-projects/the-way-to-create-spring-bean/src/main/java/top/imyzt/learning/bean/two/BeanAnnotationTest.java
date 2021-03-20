package top.imyzt.learning.bean.two;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author imyzt
 * @date 2021/03/20
 * @description 通过Bean注解注册Bean
 */
@Configuration
public class BeanAnnotationTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(BeanAnnotationTest.class);

        for (String beanDefinitionName : annotationConfigApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        //beanAnnotationTest
        //helloWorld

    }

    @Bean
    public String helloWorld() {
        return "hello world";
    }
}