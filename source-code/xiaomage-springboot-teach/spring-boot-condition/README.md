# SpringBoot自带的Conditional

打开任意*AutoConfiguration文件，一般都有下面的条件注解，在spring-boot-autoconfigure-1.5.3.RELEASE.jar的org.springframework.boot.autoconfigure.condition包下条件注解如下：

* @ConditionalOnBean：当前容器有指定Bean的条件下。
* @ConditionalOnClass：当前类路径下有指定的类的条件下。
* @ConditionalOnExpression：基于SpEL表达式作为判断条件。
* @ConditionalOnJava：基于JVM版本作为判断条件。
* @ConditionalOnJndi：在JNDI存在的条件下查找指定的位置。
* @ConditionalOnMissingBean：当容器里没有指定Bean的情况下。
* @ConditionalOnMissingClass：当类路径下没有指定的类的条件下。
* @ConditionalOnNotWebApplication：当前项目不是WEB项目的条件下。
* @ConditionalOnProperty：指定属性是否有指定的值。
* @ConditionalOnResource：类路径是否有指定的值。
* @ConditionalOnSingleCandidate：当指定Bean在容器中只有一个，或者虽然有多个但 是指定首选的Bean。
* @ConditionalOnWebApplication：当前项目是WEB项目的条件下。

这些注解都组合了@Conditional元注解，只是使用了不同的条件（Conditional），Spring 条件注解（@Conditional）我们介绍过根据不同条件创建不同Bean。  
虽然平时使用的多，但是没怎么了解过是怎么实现的，其实就是实现`org.springframework.context.annotation.Condition`的不同子类做不同的扩展即可。   



# 手写一个
## 注解
首先写一个ConditionOnOS注解
```
/**
 * @author imyzt
 * @date 2021/01/02
 * @description 是否是对应OS
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnOSCondition.class)
public @interface ConditionOnOS {


    /**
     * 系统类型
     */
    OSType osType();


    public enum OSType {
        MAC,
        LINUX,
        WINDOWS,
        ;
    }
}
```

## 处理程序
然后写一个处理程序
```
@Order
public class OnOSCondition implements ConfigurationCondition {


    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }


    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {


        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionOnOS.class.getName());
        ConditionOnOS.OSType osType = (ConditionOnOS.OSType) attributes.get("osType");


        boolean contains = System.getProperty("os.name").toUpperCase().contains(osType.name());


        return contains;
    }
}
```

## 测试程序
最后写一个启动程序
```
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

```


# 测试

## 正向
我的电脑是Mac，所以我执行后的结果是
```
…省略
15:42:08.868 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'conditionOnTest'
15:42:08.873 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'helloWorld'
MAC --- say hello!
```

## 反向
当将注解的参数修改为 ConditionOnOS.OSType.LINUX 后，结果如下
```
15:42:59.405 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'conditionOnTest'
helloWorld bean is not exists!
```

# 讲解
其中自主要的是在注解ConditionOnOS上面的@Conditional(OnOSCondition.class)元注解，它指定了判断的处理程序是OnOSCondition.class。
其次是OnOSCondition通过实现ConfigurationCondition来重写判断逻辑。

## getConfigurationPhase()

getConfigurationPhase()用于提供给Spring告知其构建阶段，有两个：
* PARSE_CONFIGURATION： 一个是Condition应评估@Configuration类，如果此时条件不匹配，@Configuration 则不会添加该类。
* REGISTER_BEAN： 该条件不会阻止@Configuration添加类，在评估条件时，所有@Configurations都将被解析。 

ConfigurationPhase的作用并不是根据条件来判断是否加载这个配置类，**实际ConfigurationPhase控制的是过滤的时**机，是在创建Configuration类的时候过滤还是在创建bean的时候过滤（也可用条件注解的生效阶段来描述）。
此处@https://blog.csdn.net/xcy1193068639/article/details/81589489
```
enum ConfigurationPhase {


   /**
    * The {@link Condition} should be evaluated as a {@code @Configuration}
    * class is being parsed.
    * <p>If the condition does not match at this point, the {@code @Configuration}
    * class will not be added.
    */
   PARSE_CONFIGURATION,


   /**
    * The {@link Condition} should be evaluated when adding a regular
    * (non {@code @Configuration}) bean. The condition will not prevent
    * {@code @Configuration} classes from being added.
    * <p>At the time that the condition is evaluated, all {@code @Configuration}
    * classes will have been parsed.
    */
   REGISTER_BEAN
}
```

## matches()
matches()方法用于重写匹配逻辑，此方法根据自己的业务逻辑重写即可。
