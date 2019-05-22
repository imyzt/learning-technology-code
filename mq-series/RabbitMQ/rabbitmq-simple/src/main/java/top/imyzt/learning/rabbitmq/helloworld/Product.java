package top.imyzt.learning.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description 消息生产者
 */
public class Product {

    final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置rabbitmq地址
        factory.setHost("localhost");
        // 创建新连接
        Connection connection = factory.newConnection();
        // 创建交换机
        Channel channel = connection.createChannel();
        // 声明一个队列, 幂等性
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String sendMsg = "hello world!";
        /*
         * 发送消息到队列
         * exchange为空串时, 使用默认匿名交换机
         * 第二个参数为RoutingKey.
         * 匿名交换器规则: 消息发送到RoutingKey名称对应的队列中.
         */
        channel.basicPublish("", Product.QUEUE_NAME, null, sendMsg.getBytes(StandardCharsets.UTF_8));
        System.out.println("Product [x] Sent '" + sendMsg + "'");

        channel.close();
        connection.close();
    }
}
