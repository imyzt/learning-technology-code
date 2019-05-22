package top.imyzt.learning.rabbitmq.queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description 工作任务安排者(生产者)
 */
public class NewTask {

    static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置rabbitmq地址
        factory.setHost("localhost");
        // 创建新连接
        Connection connection = factory.newConnection();
        // 创建交换机
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        for (int i = 0; i < 10; i++) {
            String msg = "Hello World! " + i;
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sent '" + msg + "'");
        }

        channel.close();
        connection.close();
    }
}
