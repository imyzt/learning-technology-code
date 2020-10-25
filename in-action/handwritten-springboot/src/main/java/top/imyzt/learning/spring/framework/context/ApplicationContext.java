package top.imyzt.learning.spring.framework.context;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import top.imyzt.learning.spring.beans.BeanWrapper;
import top.imyzt.learning.spring.beans.config.BeanDefinition;
import top.imyzt.learning.spring.beans.supports.BeanDefinitionReader;
import top.imyzt.learning.spring.framework.annotations.Autowired;
import top.imyzt.learning.spring.framework.annotations.RestController;
import top.imyzt.learning.spring.framework.annotations.Service;
import top.imyzt.learning.spring.framework.exception.BeanNameExistsException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2020/10/25
 * @description 应用上下文
 */
public class ApplicationContext {

    /**
     * 存放配置目录
     */
    private String[] configLocations;

    /**
     * 读取bean配置的工具
     */
    private BeanDefinitionReader reader;

    /**
     * 保存Bean定义对象
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 存放bean(beanWrapper)对象缓存
     */
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new HashMap<>();

    /**
     * 存放bean原始对象的地方
     */
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    public ApplicationContext(String... configLocations) {

        // 先将配置目录保存起来
        this.configLocations = configLocations;

        // 1. 加载配置文件
        this.reader = new BeanDefinitionReader(configLocations);
        List<BeanDefinition> beanDefinitions = reader.doLoadBeanDefinitions();

        // 2. 将BeanDefinition对象缓存起来
        doRegistryBeanDefinition(beanDefinitions);

        // 3. 创建IOC容器
        doCreateBean();
    }

    private void doCreateBean() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();

            // 此处还可以考虑增加延迟加载等功能
            getBean(beanName);
        }
    }

    private void doRegistryBeanDefinition(List<BeanDefinition> beanDefinitions) {

        for (BeanDefinition beanDefinition : beanDefinitions) {

            if (beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new BeanNameExistsException(beanDefinition.getFactoryBeanName());
            }

            // 两份都缓存
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }

    }

    /**
     * 两个职责
     * 1. 不存在则创建bean
     * 2. 完成依赖注入
     * @param beanName bean的名称
     * @return bean对象
     */
    public Object getBean(String beanName) {

        // 1. 拿到bean对应的配置信息, 即beanDefinition对象, 根据配置信息创建对象
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        // 2. 拿到BeanDefinition对象后, 进行实例化
        Object instance = instaniateBean(beanName, beanDefinition);
        if (Objects.isNull(instance)) {
            return null;
        }

        // 3. 将实例封装成BeanWrapper, 存放起来
        BeanWrapper beanWrapper = new BeanWrapper(instance);

        // 4. 将beanWrapper对象缓存到IOC容器中
        factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 5. 完成依赖注入
        populateBean(beanName, beanDefinition, beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrapperInstance();
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {


        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrapperInstanceClazz();

        // 找到bean的所有public/private/protected/default的字段
        Field[] allFields = clazz.getDeclaredFields();

        if (ArrayUtil.isEmpty(allFields)) {
            return;
        }

        for (Field field : allFields) {

            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }

            Autowired annotation = field.getAnnotation(Autowired.class);

            String autowiredBeanName = annotation.value().trim();

            if (StrUtil.isBlank(autowiredBeanName)) {
                // 如果没有指定, 直接获取字段类型
                autowiredBeanName = field.getType().getName();
            }

            field.setAccessible(true);
            try {

                if (!factoryBeanInstanceCache.containsKey(autowiredBeanName)) {
                    continue;
                }

                field.set(instance, factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
                System.out.println("DI field Name=" + field.getName() + ", bean=" + autowiredBeanName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    private Object instaniateBean(String beanName, BeanDefinition beanDefinition) {

        String beanClassName = beanDefinition.getBeanClassName();

        Object instance = null;

        try {
            Class<?> clazz = Class.forName(beanClassName);

            // 不需要托管, 直接返回
            if (!(clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(RestController.class))) {
                return null;
            }

            // 创建原生对象
            instance = clazz.newInstance();

            // AOP会配置切面表达式
            // execution(public * top.imyzt.learing..........)
            // 如果匹配上, 就要创建代理对象, 否则返回原生对象
            // 这里就是AOP入口

            // 三级缓存
            factoryBeanObjectCache.put(beanName, instance);


        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public Object getBean(Class<?> clazz) {
        return getBean(clazz.getName());
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.entrySet().toArray(new String[this.beanDefinitionMap.size()]);
    }
}