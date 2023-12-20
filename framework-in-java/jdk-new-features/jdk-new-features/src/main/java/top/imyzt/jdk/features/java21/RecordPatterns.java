package top.imyzt.jdk.features.java21;


/**
 * @author imyzt
 * @date 2023/12/19
 * @description 记录模式
 */
public class RecordPatterns {

    public static void main(String[] args) {

        Object p = new Point(1, 2);
        if (p instanceof Point(int x, int y)) {
            System.out.println(x + y);
        }

        Object w = new Window(new Point(1, 2), 3);
        if (w instanceof Window(Point(int x, int y), int z)) {
            System.out.println(x + y + z);
        }

    }
}

record Point(int x, int y) {
}

record Window(Point p, int z) {
}
