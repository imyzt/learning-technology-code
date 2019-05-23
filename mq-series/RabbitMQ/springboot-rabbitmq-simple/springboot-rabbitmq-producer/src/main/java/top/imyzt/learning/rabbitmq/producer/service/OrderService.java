package top.imyzt.learning.rabbitmq.producer.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.imyzt.learning.rabbitmq.common.entity.BrokerMessageLog;
import top.imyzt.learning.rabbitmq.common.entity.Order;
import top.imyzt.learning.rabbitmq.common.utils.Constant;
import top.imyzt.learning.rabbitmq.producer.mapper.BrokerMessageLogMapper;
import top.imyzt.learning.rabbitmq.producer.mapper.OrderMapper;
import top.imyzt.learning.rabbitmq.producer.sender.RabbitOrderSender;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description 订单服务
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private RabbitOrderSender orderSender;
    @Resource
    private BrokerMessageLogMapper messageLogMapper;

    /**
     * 创建订单
     * @param order 订单对象
     */
    public void createOrder(Order order) {

        LocalDateTime now = LocalDateTime.now();

        // 业务数据入库
        orderMapper.insert(order);

        // 构建消息日志记录对象
        BrokerMessageLog messageLog = new BrokerMessageLog();
        messageLog.setMessageId(order.getMessageId())
                .setMessage(JSON.toJSONString(order))
                .setStatus(Constant.ORDER_SENDING)
                .setNextRetry(now.plusMinutes(Constant.ORDER_TIMEOUT_MINUTES))
                .setCreateTime(now)
                .setUpdateTime(now);
        // 消息日志对象入库
        messageLogMapper.insert(messageLog);

        // rabbitmq 发送消息
        orderSender.sendOrder(order);
    }
}
