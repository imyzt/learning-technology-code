package top.imyzt.jdk.features.deadlockdemo;


/**
 * @author imyzt
 * @date 2024/12/10
 * @description jstack pid
 */
public class Demo {

    public static void main(String[] args) {
        Object locka = new Object();
        Object lockb = new Object();
        new Thread("t1"){
            @Override
            public void run() {
                synchronized (locka) {
                    System.out.println("t1 持有 A");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lockb) {
                        System.out.println("t1 持有 B");
                    }
                }
            }
        }.start();

        new Thread("t2"){
            @Override
            public void run() {
                synchronized (lockb) {
                    System.out.println("t2 持有 B");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (locka) {
                        System.out.println("t2 持有 A");
                    }
                }
            }
        }.start();

        // Found one Java-level deadlock:
        //=============================
        //"t1":
        //  waiting to lock monitor 0x0000600002bc8000 (object 0x000000070fc25d30, a java.lang.Object),
        //  which is held by "t2"
        //
        //"t2":
        //  waiting to lock monitor 0x0000600002bd41a0 (object 0x000000070fc25d20, a java.lang.Object),
        //  which is held by "t1"
        //
        //Java stack information for the threads listed above:
        //===================================================
        //"t1":
        //	at top.imyzt.jdk.features.deadlockdemo.Demo$1.run(Demo.java:25)
        //	- waiting to lock <0x000000070fc25d30> (a java.lang.Object)
        //	- locked <0x000000070fc25d20> (a java.lang.Object)
        //"t2":
        //	at top.imyzt.jdk.features.deadlockdemo.Demo$2.run(Demo.java:42)
        //	- waiting to lock <0x000000070fc25d20> (a java.lang.Object)
        //	- locked <0x000000070fc25d30> (a java.lang.Object)
        //
        //Found 1 deadlock.
    }
}
