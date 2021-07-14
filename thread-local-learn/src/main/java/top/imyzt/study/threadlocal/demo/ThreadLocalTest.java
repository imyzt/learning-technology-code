package top.imyzt.study.threadlocal.demo;

/**
 * @author imyzt
 * @date 2020/02/09
 */
public class ThreadLocalTest {

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<Long>() {
        /**
         * 针对每个线程, 调用值为NULL时都会做初始化
         */
        @Override
        protected Long initialValue() {
            // 不调用get()方法时,不会调用initialValue()
            System.out.println("init");
            return Thread.currentThread().getId();
        }
    };

    public static void main(String[] args) {

        System.out.println(THREAD_LOCAL.get());
        System.out.println("---");

//        new Thread(() -> System.out.println(THREAD_LOCAL.get())).start();

        THREAD_LOCAL.set(1111L);
        THREAD_LOCAL.remove();
        // 1,不是null,main线程默认id为1
        System.out.println(THREAD_LOCAL.get());
        System.out.println("---");

    }
}
