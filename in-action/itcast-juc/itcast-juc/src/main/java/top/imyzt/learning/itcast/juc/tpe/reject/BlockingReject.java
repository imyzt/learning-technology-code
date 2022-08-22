package top.imyzt.learning.itcast.juc.tpe.reject;

import top.imyzt.learning.itcast.juc.tpe.BlockingQueue;

/**
 * @author imyzt
 * @date 2022/08/22
 * @description 队列已满时, 死等
 */
public class BlockingReject implements RejectPolicy<Runnable> {
    @Override
    public void reject(BlockingQueue<Runnable> queue, Runnable task) {
        queue.put(task);
    }
}