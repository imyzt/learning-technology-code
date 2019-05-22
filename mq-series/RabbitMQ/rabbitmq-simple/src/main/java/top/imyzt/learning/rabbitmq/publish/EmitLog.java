package top.imyzt.learning.rabbitmq.publish;

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
 * @description fanout模式的生产者
 */
public class EmitLog {

    static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        for (int i = 0; i < 10; i++) {
            String msg = "hello world! " + i;

            // fanout模式下, 如果当前队列没有被绑定到交换器, 消息将被丢弃
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sent '" + msg + "'");
        }

        channel.close();
        connection.close();
    }
}
