package top.imyzt.jdk.features.java14;


/**
 * @author imyzt
 * @date 2023/12/16
 * @description instanceof 优化
 */
public class InstanceofFuture {

    public static void main(String[] args) {

        // Java 14之前
        Object o = "str";
        if (o instanceof String) {
            String str = (String) o;
            System.out.println(str);
        }

        // Java 14之后
        if (o instanceof String str) {
            System.out.println(str);
        }
    }
}
