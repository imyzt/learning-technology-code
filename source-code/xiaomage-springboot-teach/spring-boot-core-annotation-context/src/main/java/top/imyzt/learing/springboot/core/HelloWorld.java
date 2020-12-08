package top.imyzt.learing.springboot.core;

import org.springframework.stereotype.Component;

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
}