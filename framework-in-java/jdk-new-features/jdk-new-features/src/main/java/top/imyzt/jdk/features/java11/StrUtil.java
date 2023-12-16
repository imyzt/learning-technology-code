package top.imyzt.jdk.features.java11;


/**
 * @author imyzt
 * @date 2023/12/16
 * @description String 补充工具方法
 */
public class StrUtil {

    public static void main(String[] args) {
        //Unicode空白字符
        char c = '\u2000';
        String str = c + "abc" + c;
        // 去除普通空白字符
        System.out.println(str.trim());
        // 去除Unicode空白字符
        System.out.println(str.strip());
        // 去除前面的空白字符
        System.out.println(str.stripLeading());
        // 去除后面的空白字符
        System.out.println(str.stripTrailing());
        // abc 
        //abc
        //abc 
        // abc

        // 判空
        System.out.println(" ".isBlank());
        // 支持直接定义常量使用format
        System.out.println("%s_%s".formatted("a", "b"));
        // 重复字符串
        System.out.println("abc".repeat(3));
        //true
        //a_b
        //abcabcabc

        // lambda 类型推断
        // java11前
        MyFunc s1 = (String a, Integer b) -> a + b;
        MyFunc s2 = (a, b) -> a + b;
        // java11后, 支持类型推断(作用不大)
        MyFunc s3 = (var a, var b) -> a + b;

    }
}

@FunctionalInterface
interface MyFunc {
    String foo(String a, Integer b);
}
