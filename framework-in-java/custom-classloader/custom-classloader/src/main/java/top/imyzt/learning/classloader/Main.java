package top.imyzt.learning.classloader;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author imyzt
 * @date 2024/06/18
 * @description 描述信息
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CustomClassLoader customClassLoader = new CustomClassLoader();
        Class<?> obj = Class.forName("top.imyzt.learning.classloader.HelloWorld", true, customClassLoader);
        Object o = obj.getDeclaredConstructor().newInstance();
        Method sayHello = obj.getDeclaredMethod("sayHello");
        sayHello.invoke(o);
    }
}
