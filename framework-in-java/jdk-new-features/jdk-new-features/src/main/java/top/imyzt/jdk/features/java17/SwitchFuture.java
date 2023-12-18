package top.imyzt.jdk.features.java17;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * @author imyzt
 * @date 2023/12/17
 * @description switch 类型推断
 */
public class SwitchFuture {

    public static void main(String[] args) {

        Animal animal = new Cat();
        switch (animal) {
            case Cat c -> c.say();
            case Dog d -> d.say();
            case null -> System.out.println("null");
            default -> System.out.println("default");
        }

        /*
        ➜  java17 git:(master) ✗ java -version
        openjdk version "21.0.1" 2023-10-17 LTS
        OpenJDK Runtime Environment Corretto-21.0.1.12.1 (build 21.0.1+12-LTS)
        OpenJDK 64-Bit Server VM Corretto-21.0.1.12.1 (build 21.0.1+12-LTS, mixed mode, sharing)
        ➜  java17 git:(master) ✗ java SwitchFuture.java
        汪汪汪
         */

        RandomGeneratorFactory<RandomGenerator> factory = RandomGeneratorFactory.getDefault();
        RandomGenerator randomGenerator = factory.create();
        randomGenerator.ints(10).forEach(System.out::println);
        //261824154
        //540138312
        //-1600972486
        //-467718820
        //-660092685
        //-1149689401
        //-46916737
        //2110685130
        //-1910355456
        //-814203516
    }
}

class Animal {

}

class Cat extends Animal {
    void say() {
        System.out.println("汪汪汪");
    }
}

class Dog extends Animal {
    void say() {
        System.out.println("喵喵喵");
    }
}
