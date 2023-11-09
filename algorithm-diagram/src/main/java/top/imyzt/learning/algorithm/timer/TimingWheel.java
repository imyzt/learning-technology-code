package top.imyzt.learning.algorithm.timer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class TimingWheel {

    private static final LinkedList<LinkedList<Task>> hourWheel = new LinkedList<>();
    private static final LinkedList<LinkedList<Task>> minuteWheel = new LinkedList<>();
    private static final LinkedList<LinkedList<Task>> secondWheel = new LinkedList<>();
    private static final AtomicInteger minuteAtomic = new AtomicInteger(0);
    private static final AtomicInteger secondAtomic = new AtomicInteger(0);

    private static final Queue<Task> taskQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {

        fill(hourWheel, 12);
        fill(minuteWheel, 60);
        fill(secondWheel, 60);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int hour = LocalDateTime.now().getHour();
            LinkedList<Task> tasks = hourWheel.get(hour);
            if (tasks != null && !tasks.isEmpty()) {
                System.out.println("hour =>" + tasks.size());
                for (Task task : tasks) {
                    addTask(task);
                    hourWheel.remove(hour);
                }
            }
        }, 0, 1, TimeUnit.HOURS);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int minute = minuteAtomic.getAndAdd(1);
            if (minute >= 60) {
                minuteAtomic.set(0);
            }
            LinkedList<Task> tasks = minuteWheel.get(minute);
            if (tasks != null && !tasks.isEmpty()) {
                System.out.println("minute =>" + tasks.size());
                for (Task task : tasks) {
                    addTask(task);
                    minuteWheel.remove(minute);
                }
            }
        }, 0, 1, TimeUnit.MINUTES);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int second = secondAtomic.getAndAdd(1);
            if (second >= 60) {
                secondAtomic.set(0);
            }
            LinkedList<Task> tasks = secondWheel.get(second);
            if (tasks != null && !tasks.isEmpty()) {
                System.out.println("second =>" + tasks.size());
                for (Task task : tasks) {
                    taskQueue.add(task);
                    secondWheel.remove(second);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        CompletableFuture.runAsync(() -> {

            while (true) {
                try {
                    Task task = taskQueue.poll();
                    if (task != null) {
                        System.out.println(task.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    Random random = new Random();
                    int delayTime = random.nextInt(10);
                    System.out.println("新生产一个任务, 延迟" + delayTime + "秒后执行" + ", 当前时间: " +
                            LocalDateTime.now() + ", 预计执行时间: " + LocalDateTime.now().plusSeconds(delayTime)
                            );
                    Task task = new Task(() -> Thread.currentThread().getName(), delayTime);
                    addTask(task);
                    TimeUnit.SECONDS.sleep(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TimeUnit.HOURS.sleep(10000);
    }

    private static void fill(LinkedList<LinkedList<Task>> wheel, int size) {
        for (int i = 0; i < size; i++) {
            wheel.add(i, new LinkedList<>());
        }
    }

    private static void addTask( Task task) {

        int delayTime = task.getDelayTime();
        int hour = delayTime / (60 * 60);
        int minute = delayTime / (60);
        int second = delayTime - (hour*(60 * 60)) - (minute*(60));

        if (hour > 0) {
            task.setDelayTime(delayTime - (hour*(12 * 60 * 60)));
            add(hourWheel, task, hour);
        } else if (minute > 0) {
            task.setDelayTime(delayTime - (minute*(12 * 60)));
            add(minuteWheel, task, minute+minuteAtomic.get());
        } else if (second > 0) {
            add(secondWheel, task, second);
        }
    }

    private static void add(AtomicInteger atomic, int time) {
        if (atomic.get()+time>60) {
            // TODO: 2023/11/9 new task应该在当前时间+间隔时间上
        }
    }

    private static void add(LinkedList<LinkedList<Task>> wheel, Task task, int index) {
        if (wheel.size() - 1 < index) {
            LinkedList<Task> tasks = new LinkedList<>();
            tasks.add(task);
            wheel.set(index, tasks);
        }
        LinkedList<Task> tasks = wheel.get(index);
        if (tasks == null) {
            tasks = new LinkedList<>();
        }
        tasks.add(task);
    }

}


class Task {

    private final Integer taskId;

    /**
     * 执行任务
     */
    private final Supplier<String> runner;

    /**
     * 当前第几轮
     */
    private  Integer cycle;

    private Integer delayTime;

    /**
     * 创建时间
     */
    private final LocalDateTime createdAt;

    /**
     * 理应执行时间
     */
    private final LocalDateTime runnerTime;

    public Task(Supplier<String> runner,  Integer delayTime) {
        this.taskId = new Random().nextInt() * 10000;
        this.runner = runner;
        this.delayTime = delayTime;
        this.createdAt = LocalDateTime.now();
        this.runnerTime = this.createdAt.plusSeconds(delayTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", runner=" + runner.get() +
                ", cycle=" + cycle +
                ", delayTime=" + delayTime +
                ", createdAt=" + createdAt +
                ", runnerTime=" + runnerTime +
                '}';
    }

    public Supplier<String> getRunner() {
        return runner;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Integer getDelayTime() {
        return delayTime;
    }
}