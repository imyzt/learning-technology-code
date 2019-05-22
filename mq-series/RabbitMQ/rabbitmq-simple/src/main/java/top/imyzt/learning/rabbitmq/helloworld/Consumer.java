package top.imyzt.learning.rabbitmq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description 消息消费者
 */
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置rabbitmq地址
        factory.setHost("localhost");
        // 创建新连接
        Connection connection = factory.newConnection();
        // 创建交换机
        Channel channel = connection.createChannel();

        // 声明需要关注的队列
        channel.queueDeclare(Product.QUEUE_NAME, false, false, false, null);
        System.out.println("Consumer [*] Waiting for message, To exit press Ctrl + C");

        // DefaultConsumer 类实现了Consumer接口, 通过传入一个交换机, 告诉服务器我们需要哪个交换机的消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Consumer [x] Received '" + message + "'");
            }
        };

        // 自动回复队列应答
        channel.basicConsume(Product.QUEUE_NAME, true , consumer);
    }
}
