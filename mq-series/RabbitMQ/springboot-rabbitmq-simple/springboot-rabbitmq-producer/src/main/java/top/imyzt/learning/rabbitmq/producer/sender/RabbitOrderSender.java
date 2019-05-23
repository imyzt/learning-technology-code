package top.imyzt.learning.rabbitmq.producer.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import top.imyzt.learning.rabbitmq.common.entity.Order;
import top.imyzt.learning.rabbitmq.common.utils.Constant;
import top.imyzt.learning.rabbitmq.producer.service.BrokerMessageLogService;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description 消息可靠投递生产消息方
 */
@Component
@Slf4j
public class RabbitOrderSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private BrokerMessageLogService messageLogService;

    public void sendOrder(Order order) {

        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);

        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend(Constant.ORDER_EXCHANGE, Constant.ORDER_ROUTING, order, correlationData);
        log.info("消息已发送, messageId={}", order.getMessageId());
    }

    /**
     * 成功接收后回调, 确认消息被rabbitmq成功接收
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        String messageId = correlationData.getId();
        if (ack) {
            messageLogService.changeBrokerMessageLogStatus(messageId, Constant.ORDER_SEND_SUCCESS);
            log.info("消息发送成功, messageId={}", messageId);
        } else {
            log.error("消息发送失败, messageId={}", messageId);
        }
    }

    /**
     * 失败后回调
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        byte[] body = message.getBody();
        MessageProperties messageProperties = message.getMessageProperties();
        log.error("消息发送失败, body={}", new String(body));
    }
}
