package top.imyzt.learning.rabbitmq.consumer.receive;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.imyzt.learning.rabbitmq.common.entity.Order;

import java.util.Map;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description OrderReceive
 */
@Slf4j
@Component
public class OrderReceive {

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(
                    name = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignore-declaration-exceptions}"
            ),
            value = @Queue(
                    value = "${spring.rabbitmq.listener.order.queue.name}",
                    declare = "${spring.rabbitmq.listener.order.queue.durable}"
            ),
            key = "${spring.rabbitmq.listener.order.key}"
    ))
    public void onOrderMessage(@Payload Order order, Channel channel,
                               @Headers Map<String, Object> headers)
            throws Exception{

        // 消费消息
        log.info("-----------------------收到消息, 开始消费-----------------------");
        log.info("订单id: {}", order.getId());

        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 手工ACK
        channel.basicAck(deliveryTag, false);
    }
}
