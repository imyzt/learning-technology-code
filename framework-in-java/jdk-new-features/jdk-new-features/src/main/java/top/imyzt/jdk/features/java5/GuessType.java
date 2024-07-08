package top.imyzt.jdk.features.java5;


import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2024/06/25
 * @description 描述信息
 */
public class GuessType {

    public static void main(String[] args) {

        List<? super BlackCat> animals1 = new ArrayList<>();
        List<? extends Animal> animals2 = new ArrayList<>();

        animals1.add(new BlackCat());
        // 编译报错
        // animals1.add(new Cat());
        // // 编译报错
        // animals1.add(new Animal());
        // Object o = animals1.getFirst();
        //
        // animals2.add(new BlackCat());
        // animals2.add(new Cat());
        // animals2.add(new Animal());
    }
}

class Animal {
}

class Cat extends Animal {
}

class BlackCat extends Cat {
}
