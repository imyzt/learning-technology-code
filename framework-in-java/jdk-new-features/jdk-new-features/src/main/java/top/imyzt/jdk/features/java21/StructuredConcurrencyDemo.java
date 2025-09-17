package top.imyzt.jdk.features.java21;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

/**
 * @author imyzt
 * @date 2025/09/17
 * @description 结构化并发
 */
public class StructuredConcurrencyDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            var user = scope.fork(() -> fetchUser());
            var order = scope.fork(() -> fetchOrder());

            // 同步等待所有任务完成
            scope.join().throwIfFailed();

            System.out.println(STR."user: \{user.get()}");
            System.out.println(STR."order: \{order.get()}");

        }
    }

    private static String fetchUser() throws InterruptedException {
        Thread.sleep(1000);
        return "Alice";
    }

    private static Integer fetchOrder() throws InterruptedException {
        Thread.sleep(1500);
        return 12345;
    }
    }
