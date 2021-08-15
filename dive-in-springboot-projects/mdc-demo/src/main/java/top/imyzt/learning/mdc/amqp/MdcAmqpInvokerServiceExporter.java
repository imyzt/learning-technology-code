package top.imyzt.learning.mdc.amqp;

import com.rabbitmq.client.Channel;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 描述信息
 */
@Component
@Primary
public class MdcAmqpInvokerServiceExporter extends MessagingMessageListenerAdapter {

    private static final String TEST_MDC_ID = "testMdcId";

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        if (headers.containsKey(TEST_MDC_ID)) {
            MDC.put(TEST_MDC_ID, headers.get(TEST_MDC_ID).toString());
        }
        super.onMessage(message, channel);
    }
}