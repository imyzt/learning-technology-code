package top.imyzt.learning.mdc.amqp;

import org.slf4j.MDC;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 描述信息
 */
@Component
@Primary
public class MdcRabbitTemplate extends RabbitTemplate {

    public MdcRabbitTemplate(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    protected Message convertMessageIfNecessary(Object object) {


        if (object instanceof Message) {
            return (Message) object;
        }

        MessageProperties messageProperties = new MessageProperties();
        MDC.getCopyOfContextMap().forEach(messageProperties::setHeader);

        return getRequiredMessageConverter().toMessage(object, messageProperties);
    }

    private MessageConverter getRequiredMessageConverter() throws IllegalStateException {
        MessageConverter converter = getMessageConverter();
        if (converter == null) {
            throw new AmqpIllegalStateException(
                    "No 'messageConverter' specified. Check configuration of RabbitTemplate.");
        }
        return converter;
    }
}