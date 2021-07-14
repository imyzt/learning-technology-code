package top.imyzt.learning.mq.rabbitmq.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author imyzt
 * @date 2021/01/28
 * @description 接收者
 */
@Component
public class Receiver {

    public static final String TEST_EXCHANGE = "topic_exchange";

    public static final String  ROUTING_KEY = "test.queue";

    /**
     * 指定监听，绑定队列队列的路由和校验器
     */
    @RabbitListener(bindings ={@QueueBinding(value = @Queue,
            key = ROUTING_KEY,exchange = @Exchange(value=TEST_EXCHANGE,type = ExchangeTypes.TOPIC))})
    @RabbitHandler
    public void process(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        System.out.println(message);
        channel.basicAck(deliveryTag,true);
    }


    /**
     * 指定监听，绑定队列队列的路由和校验器
     */
    @RabbitListener(bindings ={@QueueBinding(value = @Queue,
            key = ROUTING_KEY,exchange = @Exchange(value=TEST_EXCHANGE,type = ExchangeTypes.TOPIC))})
    @RabbitHandler
    public void process2(@Payload String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        System.out.println(message);
        channel.basicAck(deliveryTag,false);
    }

}