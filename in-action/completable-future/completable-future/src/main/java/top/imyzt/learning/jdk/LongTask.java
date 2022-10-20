package top.imyzt.learning.jdk;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author imyzt
 * @date 2022/10/20
 * @description 描述信息
 */
public class LongTask implements Runnable {

    private static final Logger log = Logger.getLogger(LongTask.class.getName());

    private final int runTime;

    private final String taskName;

    public LongTask(int runTime, String taskName) {
        this.runTime = runTime;
        this.taskName = taskName;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(this.runTime);
            System.out.printf("Thread=[%s], Task[%s] is run Finish, runTime=[%s]s. %n",
                    Thread.currentThread().getName(), this.taskName, this.runTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}