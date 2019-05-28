package top.imyzt.learning.dp.proxy.c_cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import top.imyzt.learning.dp.proxy.utils.PrintUtil;

import java.lang.reflect.Method;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description CglibDynamicFactory
 */
public class CglibDynamicFactory<T> implements MethodInterceptor {

    private T target;

    public CglibDynamicFactory(T target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public T getProxyInstance() {


        // 1. 工具类
        Enhancer enhancer = new Enhancer();

        // 2. 设置父类
        enhancer.setSuperclass(target.getClass());

        // 3. 设置回调函数
        enhancer.setCallback(this);

        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        PrintUtil.print("开始事务");

        // 执行目标对象的方法
        Object returnValue = method.invoke(target, args);

        PrintUtil.print("提交事务");

        return returnValue;
    }
}
