package top.imyzt.learning.rabbitmq.topic.test1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static top.imyzt.learning.rabbitmq.topic.test1.SendTopic.EXCHANGE_NAME;


/**
 * @author imyzt
 * @date 2019/5/22
 * @description 匹配模式下
 */
public class ReceiveLogsTopic1 {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();

        String[] routingKeys = new String[]{"*"};

        // 绑定路由关键字
        for (String routingKey : routingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            System.out.println("test1  exchange:" + EXCHANGE_NAME + ", queue:" + queueName + ", BindRoutingKey:" + routingKey);
        }
        System.out.println("test1  [*] Waiting for messages. To exit press CTRL+C");


        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("test1 [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };

        // 自动ACK
        channel.basicConsume(queueName, true, consumer);
    }
}
