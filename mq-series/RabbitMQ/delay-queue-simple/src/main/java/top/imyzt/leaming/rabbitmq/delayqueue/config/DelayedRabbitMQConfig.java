package top.imyzt.leaming.rabbitmq.delayqueue.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author imyzt
 * @date 2020/08/29
 * @description 延迟插件实现消息延迟
 */
@Configuration
public class DelayedRabbitMQConfig {

    /**
     * 延迟队列
     */
    public static final String DELAYED_QUEUE_NAME = "delay.queue.demo.delay.queue";
    /**
     * 延迟交换机
     */
    public static final String DELAYED_EXCHANGE_NAME = "delay.queue.demo.delay.exchange";
    /**
     * 延迟队列 Routing Key
     */
    public static final String DELAYED_ROUTING_KEY = "delay.queue.demo.delay.routingkey";


    /**
     * 创建延迟队列
     */
    @Bean
    public Queue immediateQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    /**
     * 创建一个自定义的交换机(插件实现)
     */
    @Bean
    public CustomExchange customExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    /**
     * 绑定交换机和队列
     */
    @Bean
    public Binding bindingNotify(@Qualifier("immediateQueue") Queue queue,
                                 @Qualifier("customExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}