package top.imyzt.jdk.features.java16;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author imyzt
 * @date 2023/12/17
 * @description Integer
 */
public class IntegerFuture {

    public static void main(String[] args) {
        // 'Integer(int)' is deprecated and marked for removal
        Integer i = new Integer(2);
        System.out.println(i);

        // 不建议这样编写,因为数字在常量池中, 会和其他毫不相干的地方使用同一个锁对象
        synchronized (i) {
            System.out.println(1);
        }

        // time format
        // All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters. The following pattern letters are defined:
        // https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html
        System.out.println(DateTimeFormatter.ofPattern("B").format(LocalDateTime.now()));

        //2
        //1
        //凌晨
    }
}
