package top.imyzt.learning.condition;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author imyzt
 * @date 2021/01/02
 * @description 手写一个ConditionOn*
 */
@ComponentScan("top.imyzt.learning.condition")
@Configuration
public class ConditionOnTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();

        annotationConfigApplicationContext.register(ConditionOnTest.class);

        annotationConfigApplicationContext.refresh();

        HelloWorld helloWorld;
        try {
            helloWorld = (HelloWorld) annotationConfigApplicationContext.getBean("helloWorld");
        } catch (NoSuchBeanDefinitionException e) {
            System.out.println("helloWorld bean is not exists!");
            return;
        }

        helloWorld.say();

    }

    @Bean
    @ConditionOnOS(osType = ConditionOnOS.OSType.MAC)
    public HelloWorld helloWorld() {
        return new HelloWorld(ConditionOnOS.OSType.MAC.name());
    }

    public class HelloWorld{
        private String name;

        public HelloWorld(String name) {
            this.name = name;
        }

        public void say() {
            System.out.println(this.name + " --- say hello!");
        }
    }
}