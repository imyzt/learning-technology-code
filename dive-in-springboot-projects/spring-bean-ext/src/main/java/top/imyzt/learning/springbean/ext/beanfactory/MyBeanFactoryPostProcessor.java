package top.imyzt.learning.springbean.ext.beanfactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author imyzt
 * @date 2021/04/05
 * @description beanFactory后置处理器
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("共有bean定义: " + beanFactory.getBeanDefinitionCount());
        System.out.println("bean定义: " + Arrays.asList(beanFactory.getBeanDefinitionNames()));
    }
}