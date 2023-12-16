package main.top.imyzt.learing.jdkfuture.test;


import top.imyzt.learing.jdkfuture.dev1.Cat;
// Package 'top.imyzt.learing.jdkfuture.dev2' is declared in module 'develop', which does not export it to module 'test'
// import top.imyzt.learing.jdkfuture.dev2.Apple;

/**
 * @author imyzt
 * @date 2023/12/16
 * @description 描述信息
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Cat cat = new Cat();
        cat.eat();

        Class<?> clazz = Class.forName("top.imyzt.learing.jdkfuture.dev2.Apple");
        clazz.getDeclaredConstructor().newInstance();
    }
}
