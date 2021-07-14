package top.imyzt.study.threadlocal.demo.threadlocal;

/**
 * @author imyzt
 * @date 2020/02/15
 * @description 描述信息
 */
public class MyThreadLocalTest {

    static MyThreadLocal<Long> threadLocal = new MyThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return Thread.currentThread().getId();
        }
    };

    public static void main(String[] args) {


        for (int i = 0; i < 100; i++) {
            new Thread(() -> System.out.println(threadLocal.get())).start();
        }



    }
}