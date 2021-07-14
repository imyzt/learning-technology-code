package top.imyzt.leaming.rabbitmq.delayqueue.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码: https://www.cnblogs.com/mfrank/p/11260355.html
 * @author imyzt
 * @date 2020/08/29
 * @description 配置文件
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 延迟交换机
     */
    public static final String DELAY_EXCHANGE_NAME = "delay.queue.demo.business.exchange";
    /**
     * 延迟队列名称
     */
    private static final String DELAY_QUEUEA_NAME = "delay.queue.demo.business.queuea";
    private static final String DELAY_QUEUEB_NAME = "delay.queue.demo.business.queueb";
    /**
     * 延迟队列Routing Key
     */
    public static final String DELAY_QUEUEA_ROUTING_KEY = "delay.queue.demo.business.queuea.routingkey";
    public static final String DELAY_QUEUEB_ROUTING_KEY = "delay.queue.demo.business.queueb.routingkey";
    /**
     * 死信交换机
     */
    private static final String DEAD_LETTER_EXCHANGE = "delay.queue.demo.deadletter.exchange";
    /**
     * 死信队列Routing Key
     */
    private static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "delay.queue.demo.deadletter.delay_10s.routingkey";
    private static final String DEAD_LETTER_QUEUEB_ROUTING_KEY = "delay.queue.demo.deadletter.delay_60s.routingkey";
    /**
     * 死信队列名称
     */
    public static final String DEAD_LETTER_QUEUEA_NAME = "delay.queue.demo.deadletter.queuea";
    public static final String DEAD_LETTER_QUEUEB_NAME = "delay.queue.demo.deadletter.queueb";

    /**
     * 首先声明延迟队列, 生产者通过交换机和Routing Key将消息发送到延迟队列上
     * 然后消息变为死信时, 死信交换机将消息转发到死信队列上, 消费者对死信队列进行监听
     */

    /**
     * 声明延迟队列交换机
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }
    /**
     * 声明死信队列交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明延迟队列A
     * 延迟10秒
     * 并绑定到对应的死信交换机
     */
    @Bean
    public Queue delayQueueA() {
        Map<String, Object> args = new HashMap<>(3);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(DELAY_QUEUEA_NAME).withArguments(args).build();
    }
    /**
     * 声明延迟队列B
     * 延迟60秒
     * 并绑定到对应的死信交换机
     */
    @Bean
    public Queue delayQueueB() {
        Map<String, Object> args = new HashMap<>(3);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEB_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 60000);
        return QueueBuilder.durable(DELAY_QUEUEB_NAME).withArguments(args).build();
    }

    /**
     * 声明死信队列A, 用于接收延迟10s的消息
     */
    @Bean
    public Queue deadLetterQueueA() {
        return new Queue(DEAD_LETTER_QUEUEA_NAME);
    }
    /**
     * 声明死信队列B, 用于接收延迟60s的消息
     */
    @Bean
    public Queue deadLetterQueueB() {
        return new Queue(DEAD_LETTER_QUEUEB_NAME);
    }

    /**
     * 声明延迟队列A与延迟队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding delayBindingA(@Qualifier("delayQueueA") Queue queue,
                                                                   @Qualifier("delayExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEA_ROUTING_KEY).noargs();
    }
    /**
     * 声明延迟队列B与延迟队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding delayBindingB(@Qualifier("delayQueueB") Queue queue,
                                                                   @Qualifier("delayExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEB_ROUTING_KEY).noargs();
    }

    /**
     * 声明死信队列A与死信队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding deadBindingA(@Qualifier("deadLetterQueueA") Queue queue,
                                                                   @Qualifier("deadLetterExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEA_ROUTING_KEY).noargs();
    }
    /**
     * 声明死信队列B与死信队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding deadBindingB(@Qualifier("deadLetterQueueB") Queue queue,
                                                                  @Qualifier("deadLetterExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEB_ROUTING_KEY).noargs();
    }

}