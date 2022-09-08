package top.imyzt.learning.itcast.juc.tpe.reject;

import top.imyzt.learning.itcast.juc.tpe.BlockingQueue;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author imyzt
 * @date 2022/08/28
 * @description 拒绝执行任务
 */
public class AbortPolicy implements RejectPolicy<Runnable> {
    @Override
    public void reject(BlockingQueue<Runnable> queue, Runnable task) {
        throw new RejectedExecutionException("拒绝执行任务");
    }
}