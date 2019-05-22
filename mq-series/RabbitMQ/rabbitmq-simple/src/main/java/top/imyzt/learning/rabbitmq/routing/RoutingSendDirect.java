package top.imyzt.learning.rabbitmq.routing;

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
 * @description 消息路由模式的生产者
 */
public class RoutingSendDirect {

    static final String EXCHANGE_NAME = "direct_logs";
    /**
     * 路由关键字
     */
    static final String[] ROUTING_KEYS = new String[]{"info" ,"warning", "error"};

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        for (String routingKey : ROUTING_KEYS) {
            String msg = "Send the message level: " + routingKey;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");
        }

        channel.close();
        connection.close();
    }
}
