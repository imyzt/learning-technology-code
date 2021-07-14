package top.imyzt.learning.bean.three.beandefinition;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author imyzt
 * @date 2021/03/20
 * @description 直接注册bean
 */
public class ImportBeanDefinitionRegistrarImpl implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        // 直接注册bean
        registry.registerBeanDefinition("green", new RootBeanDefinition(Green.class));
    }
}