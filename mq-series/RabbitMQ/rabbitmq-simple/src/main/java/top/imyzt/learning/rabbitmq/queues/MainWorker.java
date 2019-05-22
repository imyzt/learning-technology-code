package top.imyzt.learning.rabbitmq.queues;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description 消息消费者
 */
public class MainWorker {

    public static void main(String[] args) {

        Thread worker01 = new Worker("Worker01");
        worker01.start();

//        Thread worker02 = new Worker("Worker02");
//        worker02.start();
    }
}
