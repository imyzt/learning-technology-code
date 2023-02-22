package top.imyzt.jdk.features.jdk13;

import java.util.Locale;

/**
 * @author imyzt
 * @date 2023/02/20
 * @description Jdk13
 */
public class Jdk13Features {

    public static void main(String[] args) {

        String test = """
                第一行  
                第二行
                第三行
                """;
        System.out.println(test);

        // 加2个空格缩进
        System.out.println(test.indent(2));

        // 减2个空格,不够就不减了
        System.out.println(test.indent(-2));

        // transform格式化字符串
        String transform = "abc".transform(str -> str.toUpperCase(Locale.ROOT));
        System.out.println(transform);

    }
}