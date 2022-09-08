package top.imyzt.learning.itcast.juc.tpe.reject;

import lombok.extern.slf4j.Slf4j;
import top.imyzt.learning.itcast.juc.tpe.BlockingQueue;

import java.util.concurrent.RejectedExecutionException;

/**
 * @author imyzt
 * @date 2022/08/28
 * @description 放弃执行任务
 */
@Slf4j
public class DiscardPolicy implements RejectPolicy<Runnable> {
    @Override
    public void reject(BlockingQueue<Runnable> queue, Runnable task) {
        log.info("放弃执行任务, task={}", task.toString());
    }
}