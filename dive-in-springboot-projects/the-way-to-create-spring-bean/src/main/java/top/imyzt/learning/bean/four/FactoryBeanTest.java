package top.imyzt.learning.bean.four;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author imyzt
 * @date 2021/03/20
 * @description factoryBean注册
 */
@Configuration
public class FactoryBeanTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(FactoryBeanTest.class);
        for (String beanDefinitionName : annotationConfigApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

        //factoryBeanTest
        //yellowFactoryBean


        Object yellowFactoryBean = annotationConfigApplicationContext.getBean("yellowFactoryBean");
        System.out.println(yellowFactoryBean.getClass());
        //class top.imyzt.learning.bean.four.Yellow

        // 获取工厂Bean本身Bean工厂
        Object yellowBean = annotationConfigApplicationContext.getBean(BeanFactory.FACTORY_BEAN_PREFIX +
                "yellowFactoryBean");
        System.out.println(yellowBean.getClass());
        // class top.imyzt.learning.bean.four.YellowFactoryBean



    }

    @Bean
    public YellowFactoryBean yellowFactoryBean() {
        return new YellowFactoryBean();
    }
}