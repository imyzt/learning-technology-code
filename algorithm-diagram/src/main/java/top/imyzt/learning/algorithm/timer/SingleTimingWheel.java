package top.imyzt.learning.algorithm.timer;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 简易单机时间轮
 * @author imyzt
 * @date 2023-11-13 22:03
 */
public class SingleTimingWheel {

    public static void main(String[] args) throws InterruptedException {

        TimingWheel timingWheel = new TimingWheel(60);
        TimingWheel timingWheel2 = new TimingWheel(12);

        while (true) {
            System.out.print("请输入延时周期: ");
            Scanner scanner = new Scanner(System.in);
            String next = scanner.next();
            if ("exit".equals(next)) {
                timingWheel.shutdown();
                timingWheel2.shutdown();
                break;
            }

            String[] splits = next.split(",");

            for (String split : splits) {
                int delayTime = Integer.parseInt(split);
                System.out.println("新生产一个任务, 延迟" + delayTime + "秒后执行" + ", 当前时间: " +
                        LocalDateTime.now() + ", 预计执行时间: " + LocalDateTime.now().plusSeconds(delayTime)
                );
                Task task = new Task(() -> Thread.currentThread().getName(), delayTime);
                timingWheel.addTask(task);
                timingWheel2.addTask(task);
            }
        }

        TimeUnit.SECONDS.sleep(1);
        System.exit(0);
    }
}

class TimingWheel {

    private final ExecutorService EXECUTOR_TASK_POOL;
    private final ScheduledExecutorService SCHEDULED_TASK_POOL;
    /**
     * 时间轮周期
     */
    private final int timer;

    /**
     * 记录每个刻度的任务
     */
    private final List<LinkedList<Task>> secondWheel;

    /**
     * 刻度计数器
     */
    private final AtomicInteger secondAtomic;

    /**
     * 任务队列
     */
    private final Queue<Task> taskQueue;

    /**
     * 运行标记
     */
    private boolean flag;

    public TimingWheel(int timer) {
        this.timer = timer;
        this.secondWheel = IntStream.range(0, timer).mapToObj(d -> new LinkedList<Task>()).collect(Collectors.toList());
        this.secondAtomic = new AtomicInteger(0);
        this.taskQueue = new LinkedBlockingQueue<>();
        this.EXECUTOR_TASK_POOL = Executors.newSingleThreadExecutor();
        this.SCHEDULED_TASK_POOL = Executors.newSingleThreadScheduledExecutor();
        this.flag = true;
        this.init();
    }

    public void addTask(Task task) {

        int delayTime = task.getDelayTime();
        int targetRunSecond = delayTime + secondAtomic.get();
        int cycle = delayTime / timer;
        int index = targetRunSecond % timer;
        task.setCycle(cycle);

        System.out.printf("任务id: %s, 当前刻度: %s, cycle: %s, 计划执行刻度: %s \n", task.getTaskId(), secondAtomic.get(), cycle, index);

        LinkedList<Task> tasks = secondWheel.get(index);
        if (tasks == null) {
            tasks = new LinkedList<>();
        }
        tasks.add(task);
    }

    public void shutdown() {
        EXECUTOR_TASK_POOL.shutdown();
        SCHEDULED_TASK_POOL.shutdown();
        this.flag = false;
        System.out.println("[" + timer + "]shutdown...");
    }

    @SneakyThrows
    private void init () {
        SCHEDULED_TASK_POOL.scheduleAtFixedRate(() -> {
            int second = secondAtomic.getAndAdd(1);
            if (second + 1 == timer) {
                secondAtomic.set(0);
            }
            LinkedList<Task> tasks = secondWheel.get(second);
            if (tasks != null && !tasks.isEmpty()) {
                Iterator<Task> iterator = tasks.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();

                    Integer taskCycle = task.getCycle();
                    if (taskCycle != 0) {
                        task.setCycle(taskCycle - 1);
                        System.out.println(task.getTaskId() + "还未到时间, 当前周期" + taskCycle);
                        continue;
                    }

                    taskQueue.add(task);
                    // 从队列中剔除
                    iterator.remove();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        EXECUTOR_TASK_POOL.execute(() -> {
            while (flag) {
                Task task = taskQueue.poll();
                if (task != null) {
                    System.out.println(LocalDateTime.now() + ", [" + timer + "]时间轮调度任务====>" + task);
                }
            }
        });
    }

}

@Getter
class Task {

    private final Integer taskId;

    /**
     * 执行任务
     */
    private final Supplier<String> runner;

    /**
     * 当前第几轮
     */
    @Setter
    private  Integer cycle;

    private final Integer delayTime;

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

}