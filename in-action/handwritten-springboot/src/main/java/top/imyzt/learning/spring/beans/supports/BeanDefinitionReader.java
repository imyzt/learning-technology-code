package top.imyzt.learning.spring.beans.supports;

import top.imyzt.learning.spring.beans.config.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author imyzt
 * @date 2020/10/25
 * @description bean定义(即配置文件)读取类
 */
public class BeanDefinitionReader {

    /**
     * 存放application.properties配置文件
     */
    private static final Properties CONTEXT_CONFIG = new Properties();

    /**
     * 存放所有的scanPackage下的java类全路径
     */
    private List<String> registryBeanClasses = new ArrayList<>();

    public BeanDefinitionReader(String[] configLocations) {

        // 1. 加载配置文件, 只有一个配置文件, 所以直接get(0)
        doLoadConfig(configLocations[0]);

        // 2. 配置文件解析
        doScanner(CONTEXT_CONFIG.getProperty("scanPackage"));

        // 3. 将配置信息封装成BeanDefinition
        doLoadBeanDefinitions();

    }

    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setFactoryBeanName(factoryBeanName);
        beanDefinition.setBeanClassName(beanClassName);
        return beanDefinition;
    }

    public List<BeanDefinition> doLoadBeanDefinitions() {
        ArrayList<BeanDefinition> result = new ArrayList<>();

        try {
            for (String beanClassName : registryBeanClasses) {

                Class<?> beanClazz = Class.forName(beanClassName);

                // 不处理接口
                if (beanClazz.isInterface()) {
                    continue;
                }

                // 默认用首字母小写做beanName
                result.add(doCreateBeanDefinition(toLowerFirstName(beanClazz.getSimpleName()), beanClazz.getName()));

                // 如果这个bean实现了其他的接口, 将这个接口也注册一个bean的实现方法
                for (Class<?> clazzInterface : beanClazz.getInterfaces()) {
                    // 接口使用全类名做key
                    result.add(doCreateBeanDefinition(clazzInterface.getName(), beanClassName));
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String toLowerFirstName(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {


        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));

        File file = new File(url.getFile());

        for (File listFile : file.listFiles()) {
            if (listFile.isDirectory()) {
                doScanner(scanPackage + "." + listFile.getName());
            } else {

                if (!listFile.getName().endsWith(".class")) {
                    continue;
                }

                String className = scanPackage + "." + listFile.getName().replaceAll(".class", "");
                System.out.println("scanPackage className = " + className);
                registryBeanClasses.add(className);
            }


        }
    }

    private void doLoadConfig(String configLocation) {

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(configLocation);

        try {
            CONTEXT_CONFIG.load(is);
        } catch (IOException e) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}