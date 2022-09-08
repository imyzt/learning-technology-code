package top.imyzt.learning.itcast.juc.tpe.reject;

import lombok.extern.slf4j.Slf4j;
import top.imyzt.learning.itcast.juc.tpe.BlockingQueue;

/**
 * @author imyzt
 * @date 2022/08/28
 * @description 提交线程执行
 */
@Slf4j
public class CallerRunsPolicy implements RejectPolicy<Runnable> {
    @Override
    public void reject(BlockingQueue<Runnable> queue, Runnable task) {
        task.run();
    }
}