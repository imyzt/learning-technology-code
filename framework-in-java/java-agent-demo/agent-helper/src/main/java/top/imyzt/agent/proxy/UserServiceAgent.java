package top.imyzt.agent.proxy;


import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;

/**
 * 使用ByteBuddy生成代理类, 代理含有{@link Log}注解的目标方法, 通过{@link LogInterceptor}为目标类环绕生成新代码
 * @author imyzt
 * @date 2024/08/11
 * @description <a href="https://www.cnblogs.com/yuarvin/p/16847117.html">教程</a>
 */
public class UserServiceAgent {

    /**
     * 基于字节码操作的动态子类生成技术(同Spring AOP中的 JDK代理/CGLIB代理)
     */
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        ByteBuddy byteBuddy = new ByteBuddy();
        Class<? extends UserService> loaded = byteBuddy
                .subclass(UserService.class)
                .name("top.imyzt.agent.proxy.UserServiceImpl")
                .method(ElementMatchers.isAnnotatedWith(Log.class))
                .intercept(MethodDelegation.to(new LogInterceptor()))
                .make()
                .load(ByteBuddy.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        UserService userService = loaded.getDeclaredConstructor().newInstance();
        userService.username("imyzt");
        System.out.println("-".repeat(20));
        userService.address("imyzt");
        String repeated = String.format("%-10s", "-").replace(' ', '-');
        System.out.println(repeated);
        System.out.println("-".repeat(20));
        userService.address("imyzt", "shenzhen");
    }
}
