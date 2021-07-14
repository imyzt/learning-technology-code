package top.imyzt.leaming.rabbitmq.delayqueue.web.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static top.imyzt.leaming.rabbitmq.delayqueue.config.DelayedRabbitMQConfig.DELAYED_EXCHANGE_NAME;
import static top.imyzt.leaming.rabbitmq.delayqueue.config.DelayedRabbitMQConfig.DELAYED_ROUTING_KEY;
import static top.imyzt.leaming.rabbitmq.delayqueue.config.RabbitMQConfig.*;

/**
 * @author imyzt
 * @date 2020/08/29
 * @description 消息生产者
 */
@Slf4j
@RestController
@RequestMapping("sender")
public class MessageSenderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 通过DLX和TTL实现的延迟队列
     * @param msg 消息内容
     * @param type 发送的队列
     */
    @PostMapping
    public void sender(String msg, String type) {

        log.info("当前时间：{},收到请求，msg:{},delayType:{}", LocalDateTime.now().toString(), msg, type);

        switch (type) {
            case "DELAY_10S":
                rabbitTemplate.convertAndSend(DELAY_EXCHANGE_NAME, DELAY_QUEUEA_ROUTING_KEY, msg);
                break;
            case "DELAY_60S":
                rabbitTemplate.convertAndSend(DELAY_EXCHANGE_NAME, DELAY_QUEUEB_ROUTING_KEY, msg);
                break;
            default:
                break;
        }
    }

    /**
     * 通过插件实现的延迟消息
     * @param msg 消息内容
     * @param delayTime 延迟时间, 毫秒
     */
    @PostMapping("v2")
    public void sender(String msg, Integer delayTime) {

        log.info("当前时间：{},收到请求，msg:{},delayTime:{}", LocalDateTime.now().toString(), msg, delayTime);

        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, msg, messagePostProcessor ->{
            messagePostProcessor.getMessageProperties().setDelay(delayTime);
            return messagePostProcessor;
        });
    }
}