package top.imyzt.learning.itcast.juc.tpe;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import top.imyzt.learning.itcast.juc.tpe.reject.RejectPolicy;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2022/08/22
 * @description 描述信息
 */
@Slf4j
public class ThreadPool {

    /**
     * 任务队列
     */
    private final BlockingQueue<Runnable> taskQueue;

    /**
     * 线程集合
     */
    private final HashSet<Worker> workers = new HashSet<>();

    /**
     * 核心线程数
     */
    private int coreSize;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 超时时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 拒绝策略
     */
    private final RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int capacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(capacity);
        this.rejectPolicy = rejectPolicy;
    }

    /**
     * 提交任务线程执行
     * 当任务线程足够时, 直接执行
     * 当任务线程不够时, 加入阻塞队列
     * @param task 任务
     */
    public void execute(Runnable task) {

        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.info("新增工作线程, {}", worker);
                workers.add(worker);
                worker.start();
            } else {
                taskQueue.tryPut(this.rejectPolicy, task);
            }
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    class Worker extends Thread {

        private Runnable task;

        @Override
        public void run() {

            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                log.info("执行任务, {}", task);
                try {
                    task.run();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.info("移除工作线程, {}", this);
                workers.remove(this);
            }
        }
    }
}
