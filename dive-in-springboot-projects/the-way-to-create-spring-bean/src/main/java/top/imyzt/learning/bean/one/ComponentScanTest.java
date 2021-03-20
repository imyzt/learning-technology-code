package top.imyzt.learning.bean.one;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author imyzt
 * @date 2021/03/20
 * @description 使用包扫描创建Bean
 */
@ComponentScan(value = "top.imyzt.learning.bean.one")
@Configuration
public class ComponentScanTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(ComponentScanTest.class);

        for (String beanDefinitionName : annotationConfigApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        //componentScanTest
        //userService

    }
}