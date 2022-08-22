package top.imyzt.learning.itcast.juc.tpe.reject;

import top.imyzt.learning.itcast.juc.tpe.BlockingQueue;

import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2022/08/22
 * @description 队列已满时, 等待一定时间
 */
public class BlockTimeWaitReject implements RejectPolicy<Runnable> {
    @Override
    public void reject(BlockingQueue<Runnable> queue, Runnable task) {
        queue.offer(task, 500, TimeUnit.MILLISECONDS);
    }
}