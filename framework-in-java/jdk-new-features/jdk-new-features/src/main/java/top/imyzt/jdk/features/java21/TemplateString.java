package top.imyzt.jdk.features.java21;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.util.FormatProcessor.FMT;

/**
 * @author imyzt
 * @date 2023/12/19
 * @description 模板字符串
 */
public class TemplateString {

    public static void main(String[] args) {

        String str = "world";
        String result = STR."hello \{str}";
        System.out.println(result);
        System.out.println(STR);

        String name = "yzt";
        String[] blogAddress = {"imyzt.top", "blog.imyzt.top"};
        String text = FMT."""
                My name is \{name}
                My blog address is \{blogAddress[0].toUpperCase()}, \{blogAddress[1].toLowerCase()}""";
        System.out.println(text);

        System.out.println(STR."\{Math.random()}");
        System.out.println(STR."\{Integer.MAX_VALUE}");
        System.out.println(STR."现在的时间是: \{LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}");
        int index = 0;
        System.out.println(STR."\{index++}, \{++index}");

        //hello world
        //java.lang.StringTemplate$$Lambda/0x00000001260457f0@33c7353a
        //My name is yzt
        //My blog address is IMYZT.TOP, blog.imyzt.top
        //0.9361799484353136
        //2147483647
        //现在的时间是: 2023-12-20
        //0, 2
    }
}
