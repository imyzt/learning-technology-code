package top.imyzt.learning.rabbitmq.producer.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.imyzt.learning.rabbitmq.common.entity.BrokerMessageLog;
import top.imyzt.learning.rabbitmq.common.entity.Order;
import top.imyzt.learning.rabbitmq.common.utils.Constant;
import top.imyzt.learning.rabbitmq.producer.sender.RabbitOrderSender;
import top.imyzt.learning.rabbitmq.producer.service.BrokerMessageLogService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description RetryMessageTask
 */
@Component
@Slf4j
public class RetryMessageTask {

    @Resource
    private RabbitOrderSender orderSender;

    @Resource
    private BrokerMessageLogService messageLogService;

    @Value("${system-constant.max-try-count}")
    private Integer maxTryCount;

    @Scheduled(initialDelay = 3000, fixedDelay = 10000)
    public void reSend() {

        log.debug("----------------------------定时任务开始----------------------------");

        List<BrokerMessageLog> brokerMessageLogs = messageLogService.query4StatusAndTimeoutMessage();
        log.info(Arrays.toString(brokerMessageLogs.toArray()));
        brokerMessageLogs.forEach(messageLog -> {
            if (messageLog.getTryCount() >= maxTryCount) {
                // 如果失败次数达到3次, 取消重试, 发送警报
                messageLogService.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constant.ORDER_SEND_FAILURE);
                log.error("messageId={}, 失败次数已达到[{}], 不再进行重试. 请排查", messageLog.getMessageId(), messageLog.getTryCount());
            } else {
                // 修改重试次数
                messageLogService.update4ReSend(messageLog, LocalDateTime.now());

                // 重试
                Order order = JSONObject.parseObject(messageLog.getMessage(), new TypeReference<Order>() {});
                orderSender.sendOrder(order);
                log.debug("messageId={}, 进行第{}次重试", messageLog.getMessageId(), messageLog.getTryCount());
            }
        });


    }
}
