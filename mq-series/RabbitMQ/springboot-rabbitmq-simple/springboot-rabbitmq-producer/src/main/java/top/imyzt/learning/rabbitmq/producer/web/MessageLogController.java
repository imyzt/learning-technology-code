package top.imyzt.learning.rabbitmq.producer.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.rabbitmq.common.entity.BrokerMessageLog;
import top.imyzt.learning.rabbitmq.producer.service.BrokerMessageLogService;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description MessageLogController
 */
@RestController
@RequestMapping("log")
public class MessageLogController {

    @Resource
    private BrokerMessageLogService brokerMessageLogService;

    @GetMapping("list")
    public Object list() {
        return brokerMessageLogService.list();
    }

    @GetMapping("{id}")
    public Object getOne(@PathVariable String id) {
        return brokerMessageLogService.lambdaQuery().eq(BrokerMessageLog::getMessageId, id).one();
    }
}
