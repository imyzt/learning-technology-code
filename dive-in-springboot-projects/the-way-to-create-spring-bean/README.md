# 创建SpringBean的多种方式


1. 包扫描 + 组件标注注解（@Controller、@Service等）
2. @Bean，导入第三方包里面的组件
3. @Import，快速导入一个组件
    1. @Import需要导入的Bean的Class
    2. 实现ImportSelector重写selectImports方法的实现类，自定义导入逻辑
    3. 实现ImportBeanDefinitionRegistrar重写registerBeanDefinitions方法，在方法内部直接使用BeanDefinitionRegistry.registerBeanDefinition手动注册Bean
4. 使用Spring提供的FactoryBean（工厂Bean）
