package top.imyzt.learning.mdc.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 描述信息
 */
@Component
@Primary
public class MdcRabbitListenerAnnotationBeanPostProcessor extends RabbitListenerAnnotationBeanPostProcessor {

    @Override
    protected void processAmqpListener(RabbitListener rabbitListener, Method method, Object bean, String beanName) {
        Method methodToUse = checkProxy(method, bean);
        MdcMethodRabbitListenerEndpoint endpoint = new MdcMethodRabbitListenerEndpoint();
        endpoint.setMethod(methodToUse);
        processListener(endpoint, rabbitListener, bean, methodToUse, beanName);
    }

    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @RabbitListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    }
                    catch (NoSuchMethodException noMethod) {
                    }
                }
            }
            catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            }
            catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@RabbitListener method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for a bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(), method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }
}