package top.imyzt.learning.listener.springlistener;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * 通过监听所有类型事件, 获取不同时期的Spring应用上下文状态
 * @author imyzt
 */
@SpringBootApplication
public class SpringListenerApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = new GenericApplicationContext();
		System.out.println("创建Spring应用上下文: " + context.getDisplayName());

		context.addApplicationListener(event -> System.out.println("触发事件: " + event.getClass().getSimpleName()));

		// context.refresh();初始化应用上下文
		System.out.println("应用上下文准备初始化...");
		context.refresh();
		System.out.println("应用上下文初始化完成...");

		// context.stop();停止应用上下文
		System.out.println("应用上下文准备停止...");
		context.stop();
		System.out.println("应用上下文已经停止...");

		// context.start();启动应用上下文
		System.out.println("应用上下文准备重新启动...");
		context.start();
		System.out.println("应用上下文已经重新启动...");

		// context.close();关闭应用上下文事件
		System.out.println("应用上下文准备关闭...");
		context.close();
		System.out.println("应用上下文已经关闭...");

		//创建Spring应用上下文: org.springframework.context.support.GenericApplicationContext@d70c109
		//应用上下文准备初始化...
		//10:17:39.672 [main] DEBUG org.springframework.context.support.GenericApplicationContext - Refreshing org.springframework.context.support.GenericApplicationContext@d70c109
		//触发事件: ContextRefreshedEvent
		//10:17:39.714 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Found key 'spring.liveBeansView.mbeanDomain' in PropertySource 'systemProperties' with value of type String
		//应用上下文初始化完成...
		//应用上下文准备停止...
		//触发事件: ContextStoppedEvent
		//应用上下文已经停止...
		//应用上下文准备重新启动...
		//触发事件: ContextStartedEvent
		//应用上下文已经重新启动...
		//应用上下文准备关闭...
		//10:17:39.741 [main] DEBUG org.springframework.context.support.GenericApplicationContext - Closing org.springframework.context.support.GenericApplicationContext@d70c109, started on Sun Jan 24 10:17:39 CST 2021
		//10:17:39.742 [main] DEBUG org.springframework.core.env.PropertySourcesPropertyResolver - Found key 'spring.liveBeansView.mbeanDomain' in PropertySource 'systemProperties' with value of type String
		//触发事件: ContextClosedEvent
		//应用上下文已经关闭...
		//
		//Process finished with exit code 0

	}

}
