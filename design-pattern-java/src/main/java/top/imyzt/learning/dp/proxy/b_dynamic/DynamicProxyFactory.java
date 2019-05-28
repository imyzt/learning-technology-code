package top.imyzt.learning.dp.proxy.b_dynamic;

import top.imyzt.learning.dp.proxy.utils.PrintUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description JDK动态代理工厂
 */
public class DynamicProxyFactory<T> {

    private static final Pattern p = Pattern.compile("(query\\w+|find\\w+|get\\w)");

    /**
     * 目标对象
     */
    private T target;

    public DynamicProxyFactory(T target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public T getProxyInstance() {
        return  (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (Object proxy, Method method, Object[] args) -> {

                    Object returnValue;

                    // 模拟Spring 的事务开启开关, 根据方法名判断是否需要开启事务
                    if (checkMethod(method)) {
                        // 执行目标对象的方法
                        return method.invoke(target, args);
                    }

                    PrintUtil.print("开始事务");

                    // 执行目标对象的方法
                    returnValue = method.invoke(target, args);

                    PrintUtil.print("结束事务");

                    return returnValue;
                });
    }


    /**
     * 检查方法是否需要开启事务. 根据方法名简单判断
     * @param method 方法
     */
    private boolean checkMethod(Method method) {
        String name = method.getName();
        Matcher matcher = p.matcher(name);
        return matcher.find();
    }
}
