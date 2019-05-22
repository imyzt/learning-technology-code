package top.imyzt.learning.rabbitmq.topic.test2;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description 匹配模式下的生产者
 */
public class SendTopic {

    static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明一个匹配模式的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 待发送的消息
        String[] routingKeys = new String[]{"..", "a"};

        for (String routingKey : routingKeys) {
            String msg = "Form " + routingKey + "routingKey's message";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("test2 [x] Sent '" + routingKey + "':'" + msg + "'");
        }

        channel.close();
        connection.close();
    }
}
