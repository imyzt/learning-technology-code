package top.imyzt.learning.listener.springlistener;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.GenericApplicationContext;

/**
 * 通过泛型监听, 测试在关闭应用上下文后,是否能继续收到监听事件
 *
 * 结果是无法收到监听事件, 因为在close()方法的doClose()中, destroyBeans()将所有Listener从ApplicationEventMulticaster关联缓存中删除
 *
 * 删除位置:
 * org.springframework.context.support.ApplicationListenerDetector#postProcessBeforeDestruction(java.lang.Object, java.lang.String)
 * @author imyzt
 * @date 2021/01/24
 */
@SpringBootApplication
public class GenericListenerApplication {

    public static void main(String[] args) {

        // 创建Spring应用上下文GenericApplicationContext
        GenericApplicationContext context = new GenericApplicationContext();

        // 注册自定义监听器
        context.registerBean(MyApplicationListener.class);

        // 初始化上下文
        context.refresh();

        // 发布自定义事件
        context.publishEvent(new MyApplicationEvent("hello listener"));

        // 关闭上下文
        context.close();
        // 再次发布事件
        context.publishEvent(new MyApplicationEvent("hello listener again"));

        //10:29:08.426 [main] DEBUG org.springframework.context.support.GenericApplicationContext - Refreshing org.springframework.context.support.GenericApplicationContext@d70c109
        //10:29:08.451 [main] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'top.imyzt.learning.listener.springlistener.GenericListenerApplication$MyApplicationListener'
        //10:29:08.475 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Found key 'spring.liveBeansView.mbeanDomain' in PropertySource 'systemProperties' with value of type String
        //MyApplicationEvent: hello listener
        //10:29:08.497 [main] DEBUG org.springframework.context.support.GenericApplicationContext - Closing org.springframework.context.support.GenericApplicationContext@d70c109, started on Sun Jan 24 10:29:08 CST 2021
        //10:29:08.497 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Found key 'spring.liveBeansView.mbeanDomain' in PropertySource 'systemProperties' with value of type String
    }

    /**
     * 自定义监听事件
     */
    static class MyApplicationEvent extends ApplicationEvent {

        MyApplicationEvent(Object source) {
            super(source);
        }
    }

    /**
     * 自定义事件监听器
     */
    public static class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {

        @Override
        public void onApplicationEvent(MyApplicationEvent event) {
            System.out.println(event.getClass().getSimpleName() + ": " + event.getSource());
        }
    }
}