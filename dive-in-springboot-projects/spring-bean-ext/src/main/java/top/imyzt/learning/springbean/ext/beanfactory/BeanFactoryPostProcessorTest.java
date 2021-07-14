package top.imyzt.learning.springbean.ext.beanfactory;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.imyzt.learning.springbean.ext.common.Red;

/**
 * @author imyzt
 * @date 2021/04/05
 * @description BeanFactoryPostProcessor
 */
@Configuration
@ComponentScan("top.imyzt.learning.springbean.ext.beanfactory")
public class BeanFactoryPostProcessorTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanFactoryPostProcessorTest.class);

        context.close();

        // 纯MyBeanFactoryPostProcessor加载:
        //共有bean定义: 8
        //bean定义: [org.springframework.context.annotation.internalConfigurationAnnotationProcessor, org.springframework.context.annotation.internalAutowiredAnnotationProcessor, org.springframework.context.annotation.internalCommonAnnotationProcessor, org.springframework.context.event.internalEventListenerProcessor, org.springframework.context.event.internalEventListenerFactory, beanFactoryPostProcessorTest, myBeanFactoryPostProcessor, red]
        //15:22:17.031 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'red'
        //red constructor...

        // 增加了MyBeanDefinitionRegistryPostProcessor加载:
        //MyBeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry beanCount=9
        //MyBeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry beanCount=10
        //共有bean定义: 10
        //bean定义: [org.springframework.context.annotation.internalConfigurationAnnotationProcessor, org.springframework.context.annotation.internalAutowiredAnnotationProcessor, org.springframework.context.annotation.internalCommonAnnotationProcessor, org.springframework.context.event.internalEventListenerProcessor, org.springframework.context.event.internalEventListenerFactory, beanFactoryPostProcessorTest, myBeanDefinitionRegistryPostProcessor, myBeanFactoryPostProcessor, red, blue]
        //red constructor...
        //blue constructor...
    }

    @Bean
    public Red red() {
        return new Red();
    }
}