package top.imyzt.learing.springboot.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author imyzt
 * @date 2020/12/08
 * @description AnnotationConfigApplicationContext
 */
// 声明配置类
@Configuration
// 声明bean扫描路径
@ComponentScan(basePackages = "top.imyzt.learing.springboot.core")
public class AnnotationConfigApplicationContextTest {


    public static void main(String[] args) {

        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();

        // 将当前配置类注册到Spring上下文
        annotationConfigApplicationContext.register(AnnotationConfigApplicationContextTest.class);

        // 启动上下文
        annotationConfigApplicationContext.refresh();

        // 获取bean
        HelloWorld helloWorld = annotationConfigApplicationContext.getBean(HelloWorld.class);

        helloWorld.sayHello();

    }

}