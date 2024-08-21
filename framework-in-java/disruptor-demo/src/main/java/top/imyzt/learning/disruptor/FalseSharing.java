package top.imyzt.learning.disruptor;


/**
 * CPU并不直接从内存中获取数据,而是从CPU Cache中获取,且从中获取通常是以[缓存行]的形式一次64字节(通常)为单位获取,
 * java的long类型是8字节,因此一个缓存行可以存放8个long类型的变量
 *
 * 而像如下代码中{@link Pointer}的x和y,因为被线程 t1,t2 读取,在多核CPU下,线程1对应的CPU1在读取数据时,会一并把x/y...(64字节)的数据一并读取到CPU1,
 * 后面线程1修改了x之后,写回内存时,由于x和y都使用volatile变量修饰保持了内存可见性,线程2对应的CPU2读取到y时,发现这个缓存行已经失效了,需要重新从主内存加载
 *
 * 这就是伪内存,x和y不相干,但是却因为x的更新导致需要从主内存获取,拖慢了程序性能,解决办法就是在xy之间增加7个long类型的变量,保证x和y不会被加载到同一个缓存行中去
 *
 * @author imyzt
 * @date 2024/08/21
 * @description 伪共享
 */
public class FalseSharing {

    public static void main(String[] args) throws InterruptedException {
        int num = 100000000;
        Pointer pointer1 = new Pointer();
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < num; i++){
                pointer1.x++;
            }
        });
        Thread t2 = new Thread(() -> {
            for(int i = 0; i < num; i++){
                pointer1.y++;
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("pointer1=" + (System.currentTimeMillis() - start));

        Pointer2 pointer2 = new Pointer2();
        long start2 = System.currentTimeMillis();
        Thread t3 = new Thread(() -> {
            for(int i = 0; i < num; i++){
                pointer2.x++;
            }
        });
        Thread t4 = new Thread(() -> {
            for(int i = 0; i < num; i++){
                pointer2.y++;
            }
        });
        t3.start();
        t4.start();
        t3.join();
        t4.join();
        System.out.println("pointer2=" + (System.currentTimeMillis() - start2));

        // pointer1=3545
        // pointer2=549
    }
}

class Pointer {

    volatile long x;

    volatile long y;
}

class Pointer2 {
    volatile long x;
    // long类型,占用8个字节, 8*7=56
    // long p1, p2, p3, p4, p5, p6, p7;
    // 包装Long类型,根据计算机不同有所不同,在我电脑上占用24字节, 24+24+8=56
    Long z1, z2; long z3;
    volatile long y;
}
