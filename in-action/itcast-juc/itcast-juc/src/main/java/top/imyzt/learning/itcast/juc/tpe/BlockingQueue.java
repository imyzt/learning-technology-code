package top.imyzt.learning.itcast.juc.tpe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.imyzt.learning.itcast.juc.tpe.reject.RejectPolicy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author imyzt
 * @date 2022/08/22
 * @description 描述信息
 */
@RequiredArgsConstructor
@Slf4j
public class BlockingQueue<T> {

    /**
     * 任务队列
     */
    private final Deque<T> taskQueue = new ArrayDeque<>();

    /**
     * 对象锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 生产者条件变量
     */
    private final Condition fullWaitSet = lock.newCondition();

    /**
     * 消费者条件变量
     */
    private final Condition emptyWaitSet = lock.newCondition();

    /**
     * 容量
     */
    private final int capacity;

    /**
     * 带超时的阻塞获取元素方法
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 元素
     */
    public T poll(long timeout, TimeUnit timeUnit) {
        lock.lock();

        try {
            long nanos = timeUnit.toNanos(timeout);
            while (taskQueue.isEmpty()) {
                if (nanos <= 0) {
                    return null;
                }
                // 限时等待, 返回的是剩余时间
                // 防止emptyWaitSet.signal()通知所有时,出现假唤醒的情况
                // 如果被唤醒, 则继续睡眠 剩余睡眠时间 个单位
                nanos = emptyWaitSet.awaitNanos(nanos);
            }
            T element = taskQueue.removeFirst();
            fullWaitSet.signal();
            return element;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞获取元素
     * @return 元素
     */
    public T take() {
        lock.lock();

        try {
            while (taskQueue.isEmpty()) {
                emptyWaitSet.await();
            }
            T element = taskQueue.removeFirst();
            fullWaitSet.signal();
            return element;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时时间的阻塞添加元素
     * @param element 元素
     */
    public boolean offer(T element, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (taskQueue.size() == capacity) {
                log.info("等待加入任务队列, {}", element);
                if (nanos <= 0) {
                    return false;
                }
                nanos = fullWaitSet.awaitNanos(nanos);
            }
            log.info("加入任务队列, {}", element);
            taskQueue.addLast(element);
            emptyWaitSet.signal();
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞添加元素
     * @param element 元素
     */
    public void put(T element) {
        lock.lock();
        try {
            while (taskQueue.size() == capacity) {
                log.info("等待加入任务队列, {}", element);
                fullWaitSet.await();
            }
            log.info("加入任务队列, {}", element);
            taskQueue.addLast(element);
            emptyWaitSet.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return taskQueue.size();
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            if (taskQueue.size() == capacity) {
                // 当出现阻塞队列已满时, 根据拒绝策略决定代码走向
                rejectPolicy.reject(this, task);
            } else {
                log.info("加入任务队列, {}", task);
                taskQueue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}