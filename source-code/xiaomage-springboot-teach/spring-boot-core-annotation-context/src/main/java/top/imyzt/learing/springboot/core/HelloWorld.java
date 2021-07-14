package top.imyzt.learing.springboot.core;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author imyzt
 * @date 2020/12/08
 * @description HelloWorld
 */
@Component
public class HelloWorld {

    public void sayHello() {
        System.out.println("hello world!");
    }

    public static void main(String[] args) {
        String regex = "\\{([^}])*}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("ab{gnfnm}ah{hell}o");
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }
}