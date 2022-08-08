package top.imyzt.learning.itcast.juc.lock.alternate;

import java.util.concurrent.locks.LockSupport;

/**
 * @author imyzt
 * @date 2022/08/08
 * @description 使用Park实现交替执行
 */
public class AlternateOutputOfPark {

    static Thread a;
    static Thread b;
    static Thread c;

    public static void main(String[] args) {


        ParkUnPark parkUnPark = new ParkUnPark(10);

        a = new Thread(() -> parkUnPark.print("a", b), "t1");
        b = new Thread(() -> parkUnPark.print("b", c), "t2");
        c = new Thread(() -> parkUnPark.print("c", a), "t3");

        a.start();
        b.start();
        c.start();

        LockSupport.unpark(a);
    }
}

class ParkUnPark {

    private final int loopNumber;

    public ParkUnPark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String content, Thread next) {
        for (int i = 0; i < this.loopNumber; i++) {
            LockSupport.park();
            System.out.print(content);
            LockSupport.unpark(next);
        }
    }
}