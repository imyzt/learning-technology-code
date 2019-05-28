package top.imyzt.learning.dp.proxy.b_dynamic;

import java.util.Arrays;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description App
 */
public class App {

    public static void main(String[] args) {

        UserDao target = new UserDao();
        // class top.imyzt.learning.dp.proxy.b_dynamic.UserDao
        System.out.println(target.getClass());

        IUserDao proxyInstance = new DynamicProxyFactory<>(target).getProxyInstance();
        // class com.sun.proxy.$Proxy0   内存中动态生成的代理对象(实现了目标对象的接口)
        System.out.println(proxyInstance.getClass());
        // [interface top.imyzt.learning.dp.proxy.b_dynamic.IUserDao]
        System.out.println(Arrays.toString(proxyInstance.getClass().getInterfaces()));

        // 需要开启事务的方法
        proxyInstance.save();

        // 不需要开启事务的方法
        proxyInstance.queryUserById(11);
    }
}
