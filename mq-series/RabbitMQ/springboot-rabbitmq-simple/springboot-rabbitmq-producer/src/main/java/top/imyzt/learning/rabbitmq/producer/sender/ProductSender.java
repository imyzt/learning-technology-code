package top.imyzt.learning.rabbitmq.producer.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.imyzt.learning.rabbitmq.common.entity.Order;

import javax.annotation.Resource;

import static top.imyzt.learning.rabbitmq.common.utils.Constant.ORDER_EXCHANGE;
import static top.imyzt.learning.rabbitmq.common.utils.Constant.ORDER_ROUTING;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description ProductSender
 */
@Component
@Slf4j
public class ProductSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(Order order) {

        CorrelationData correlationData = new CorrelationData();
        // 消息唯一ID
        correlationData.setId(order.getMessageId());
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING, order, correlationData);
    }
}
