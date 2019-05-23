package top.imyzt.learning.rabbitmq.producer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.imyzt.learning.rabbitmq.common.entity.BrokerMessageLog;
import top.imyzt.learning.rabbitmq.common.utils.Constant;
import top.imyzt.learning.rabbitmq.producer.mapper.BrokerMessageLogMapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description BrokerMessageLogService
 */
@Service
public class BrokerMessageLogService extends ServiceImpl<BrokerMessageLogMapper, BrokerMessageLog> {

    public List<BrokerMessageLog> query4StatusAndTimeoutMessage() {
        return lambdaQuery().eq(BrokerMessageLog::getStatus, Constant.ORDER_SENDING)
                .le(BrokerMessageLog::getNextRetry, LocalDateTime.now())
                .list();
    }

    public void changeBrokerMessageLogStatus(String messageId, String status) {
        boolean update = lambdaUpdate().set(BrokerMessageLog::getStatus, status)
                .eq(BrokerMessageLog::getMessageId, messageId)
                .update();
        System.out.println("&&&f" + update);
    }

    public void update4ReSend(BrokerMessageLog messageLog, LocalDateTime now) {
        lambdaUpdate().set(BrokerMessageLog::getTryCount, messageLog.getTryCount() + 1)
                .set(BrokerMessageLog::getUpdateTime, now)
                .eq(BrokerMessageLog::getMessageId, messageLog.getMessageId())
                .update();
    }
}
