package top.imyzt.leaming.rabbitmq.delayqueue.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static top.imyzt.leaming.rabbitmq.delayqueue.config.RabbitMQConfig.DEAD_LETTER_QUEUEA_NAME;
import static top.imyzt.leaming.rabbitmq.delayqueue.config.RabbitMQConfig.DEAD_LETTER_QUEUEB_NAME;

/**
 * @author imyzt
 * @date 2020/08/29
 * @description 死信队列消费者
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},死信队列A收到消息：{}", LocalDateTime.now().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = DEAD_LETTER_QUEUEB_NAME)
    public void receiveB(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},死信队列B收到消息：{}", LocalDateTime.now().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}