package top.imyzt.learning.rabbitmq.publish;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description fanout模式的消费者
 *
 * fanout模式下, 如果当前队列没有被绑定到交换器, 消息将被丢弃
 */
public class ReceiveLogs2 {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EmitLog.EXCHANGE_NAME, "fanout");
        // 生成一个临时队列, 消费者一旦断开, 队列立即被删除
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EmitLog.EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs2 [*] Waiting for messages. To exit press CTRL+C");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("ReceiveLogs2 [x] Received '" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}
