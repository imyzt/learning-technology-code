package top.imyzt.learning.dp.proxy.c_cglib;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description App
 */
public class App {

    public static void main(String[] args) {

        UserDao target = new UserDao();

        UserDao proxyInstance = new CglibDynamicFactory<>(target).getProxyInstance();

        proxyInstance.queryUserById(1);

        proxyInstance.save();
    }
}
