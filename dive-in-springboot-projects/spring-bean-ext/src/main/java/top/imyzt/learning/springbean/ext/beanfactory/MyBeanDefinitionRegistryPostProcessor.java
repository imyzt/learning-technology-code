package top.imyzt.learning.springbean.ext.beanfactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import top.imyzt.learning.springbean.ext.common.Blue;

/**
 * @author imyzt
 * @date 2021/04/05
 * @description 描述信息
 */
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("MyBeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry beanCount=" + registry.getBeanDefinitionCount());

        // RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Blue.class);

        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Blue.class).getBeanDefinition();

        registry.registerBeanDefinition("blue", beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("MyBeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry beanCount=" + beanFactory.getBeanDefinitionCount());
    }
}