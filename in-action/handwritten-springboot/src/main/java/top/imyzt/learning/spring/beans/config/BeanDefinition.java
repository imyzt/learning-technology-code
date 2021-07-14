package top.imyzt.learning.spring.beans.config;

/**
 * @author imyzt
 * @date 2020/10/25
 * @description bean定义对象, 保存bean定义配置
 */
public class BeanDefinition {

    private String factoryBeanName;

    private String beanClassName;

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}