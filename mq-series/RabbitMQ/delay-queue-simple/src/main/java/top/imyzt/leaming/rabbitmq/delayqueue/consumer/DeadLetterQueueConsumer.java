package top.imyzt.leaming.rabbitmq.delayqueue.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static top.imyzt.leaming.rabbitmq.delayqueue.config.DelayedRabbitMQConfig.DELAYED_QUEUE_NAME;
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

    /**
     * 10s延迟消费者
     */
    @RabbitListener(queues = DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},死信队列A收到消息：{}", LocalDateTime.now().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 60s延迟消费者
     */
    @RabbitListener(queues = DEAD_LETTER_QUEUEB_NAME)
    public void receiveB(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},死信队列B收到消息：{}", LocalDateTime.now().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 插件延迟队列
     * 消费者
     */
    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveD(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},延时队列收到消息：{}", LocalDateTime.now().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}