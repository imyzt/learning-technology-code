package top.imyzt.learning.itcast.juc.tpe.reject;

import top.imyzt.learning.itcast.juc.tpe.BlockingQueue;

/**
 * @author imyzt
 * @date 2022/08/22
 * @description 拒绝策略
 */
@FunctionalInterface
public interface RejectPolicy<T> {

    /**
     * 拒绝策略
     * @param queue 任务队列
     * @param task 待执行代码块
     */
    void reject(BlockingQueue<T> queue, T task);
}