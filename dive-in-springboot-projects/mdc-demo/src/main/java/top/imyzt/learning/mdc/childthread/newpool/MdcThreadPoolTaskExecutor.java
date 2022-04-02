package top.imyzt.learning.mdc.childthread.newpool;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2020/03/14
 * @description 让@Async 支持MDC链路跟踪
 */
@Slf4j
public class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private final boolean useFixedContext;
    private final Map<String, String> fixedContext;

    public MdcThreadPoolTaskExecutor(Map<String, String> fixedContext, int corePoolSize, int maximumPoolSize,
                                     long keepAliveTime, TimeUnit unit, int queueCapacity, String threadNamePrefix) {
        setCorePoolSize(corePoolSize);
        setMaxPoolSize(maximumPoolSize);
        setKeepAliveSeconds((int) unit.toSeconds(keepAliveTime));
        setQueueCapacity(queueCapacity);
        this.fixedContext = fixedContext;
        useFixedContext = (fixedContext != null);
        setThreadNamePrefix(threadNamePrefix);
        initialize();
    }

    private Map<String, String> getContextForTask() {
        return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    }

    /**
     * All executions will have MDC injected. {@code ThreadPoolExecutor}'s submission methods ({@code submit()} etc.)
     * all delegate to this.
     */
    @Override
    public void execute(@NonNull Runnable command) {
        super.execute(wrap(command, getContextForTask()));
    }

    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable task) {
        return super.submit(wrap(task, getContextForTask()));
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return super.submit(wrap(task, getContextForTask()));
    }

    private static <T> Callable<T> wrap(final Callable<T> task, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return task.call();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    private static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}
