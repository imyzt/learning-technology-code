package top.imyzt.learning.rabbitmq.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static top.imyzt.learning.rabbitmq.routing.RoutingSendDirect.EXCHANGE_NAME;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description 消息路由模式消费者, 关注所有路由key
 */
public class ReceiveLogsDirect2 {

    private static final String[] ROUTING_KEYS = new String[]{"error"};

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String queueName = channel.queueDeclare().getQueue();

        // 根据路由关键字进行多重绑定
        for (String routingKey : ROUTING_KEYS) {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            System.out.println("ReceiveLogsDirect2 exchange:" + EXCHANGE_NAME + ", queue:" + queueName + ", BindRoutingKey:" + routingKey);
        }
        System.out.println("ReceiveLogsDirect2 [*] Waiting for messages. To exit press CTRL+C");


        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        // 自动ACK
        channel.basicConsume(queueName, true, consumer);
    }
}
