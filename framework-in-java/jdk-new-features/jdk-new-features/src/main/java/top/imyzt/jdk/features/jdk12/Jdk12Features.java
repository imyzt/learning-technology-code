package top.imyzt.jdk.features.jdk12;

import java.util.Random;

/**
 * @author imyzt
 * @date 2023/02/20
 * @description Jdk12
 */
public class Jdk12Features {

    public static void main(String[] args) {

        // 1. switch支持表达式
        int random = new Random().nextInt();
        String test = switch (random) {
            case 10 -> "10";
            case 20 -> "20";
            default -> "其它";
        };
        System.out.println(test);

        System.out.println();

        // 2. 多行文本
        String demo = """
                多行文本
                带格式
                文本
                """;
        System.out.println(demo);

    }
}