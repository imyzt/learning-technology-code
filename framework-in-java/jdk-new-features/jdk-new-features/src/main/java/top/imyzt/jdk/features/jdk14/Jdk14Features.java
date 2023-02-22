package top.imyzt.jdk.features.jdk14;

/**
 * @author imyzt
 * @date 2023/02/20
 * @description Jdk14
 */
public class Jdk14Features {

    public static void main(String[] args) {

        // 1. instanceof 模式匹配
        test("abc");
        test(1);

        System.out.println();

        // 2. Record
        RecordDemo recordDemo = new RecordDemo("张三", 18);
        System.out.println(recordDemo);
    }

    private static void test(Object str) {

        if (str instanceof String s) {
            System.out.println(s + "=str是字符串");
        } else if (str instanceof Integer i) {
            System.out.println(i + "=str是数值");
        }
    }
}