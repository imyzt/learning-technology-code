package top.imyzt.learning.mdc.amqp;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 描述信息
 */
@Component
@Primary
public class MdcMethodRabbitListenerEndpoint extends MethodRabbitListenerEndpoint {


    @Override
    protected MessagingMessageListenerAdapter createMessageListenerInstance() {
        return new MdcAmqpInvokerServiceExporter();
    }

    @Override
    protected MessagingMessageListenerAdapter createMessageListener(MessageListenerContainer container) {
        Assert.state(this.getMessageHandlerMethodFactory() != null,
                "Could not create message listener - MessageHandlerMethodFactory not set");
        MessagingMessageListenerAdapter messageListener = createMessageListenerInstance();
        messageListener.setHandlerAdapter(configureListenerAdapter(messageListener));
        String replyToAddress = getDefaultReplyToAddress();
        if (replyToAddress != null) {
            messageListener.setResponseAddress(replyToAddress);
        }
        MessageConverter messageConverter = getMessageConverter();
        if (messageConverter == null) {
            // fall back to the legacy converter holder in the container
            messageConverter = container.getMessageConverter();
        }
        if (messageConverter != null) {
            messageListener.setMessageConverter(messageConverter);
        }
        if (getBeanResolver() != null) {
            messageListener.setBeanResolver(getBeanResolver());
        }
        return messageListener;
    }

    private String getDefaultReplyToAddress() {
        Method listenerMethod = getMethod();
        if (listenerMethod != null) {
            SendTo ann = AnnotationUtils.getAnnotation(listenerMethod, SendTo.class);
            if (ann != null) {
                String[] destinations = ann.value();
                if (destinations.length > 1) {
                    throw new IllegalStateException("Invalid @" + SendTo.class.getSimpleName() + " annotation on '"
                            + listenerMethod + "' one destination must be set (got " + Arrays.toString(destinations) + ")");
                }
                return destinations.length == 1 ? resolveSendTo(destinations[0]) : "";
            }
        }
        return null;
    }

    private String resolveSendTo(String value) {
        if (getBeanFactory() != null) {
            String resolvedValue = getBeanExpressionContext().getBeanFactory().resolveEmbeddedValue(value);
            Object newValue = getResolver().evaluate(resolvedValue, getBeanExpressionContext());
            Assert.isInstanceOf(String.class, newValue, "Invalid @SendTo expression");
            return (String) newValue;
        }
        else {
            return value;
        }
    }
}