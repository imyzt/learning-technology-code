package top.imyzt.learning.rabbitmq.queues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description Worker
 */
public class Worker extends Thread {

    Worker(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            // 设置rabbitmq地址
            factory.setHost("localhost");
            // 创建新连接
            Connection connection = factory.newConnection();
            // 创建交换机
            Channel channel = connection.createChannel();

            // 声明队列需要持久化(rabbitmq会对消息存盘)
            boolean durable = true;
            channel.queueDeclare(NewTask.TASK_QUEUE_NAME, durable, false, false, null);
            System.out.println(getName() + " [*] Waiting for messages. To exit press CTRL+C");

            // 每次从队列中获取1个
            channel.basicQos(1);

            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println(getName() + " [x] Received '" + message + "'");

                    try {
                        doWorker(message);
                    } finally {
                        System.out.println(getName() + " [x] Done");
                        // 消息处理完成确认
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            // 消息消费完成确认
            channel.basicConsume(NewTask.TASK_QUEUE_NAME, false, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    private void doWorker(String msg) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(getName() + " 处理消息, msg=" + msg);
    }
}
