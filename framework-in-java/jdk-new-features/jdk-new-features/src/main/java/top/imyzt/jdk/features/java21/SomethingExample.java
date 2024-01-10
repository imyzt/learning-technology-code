package top.imyzt.jdk.features.java21;


import java.util.List;

/**
 * @author imyzt
 * @date 2023/12/21
 * @description 加了些新方法
 */
public class SomethingExample {

    void main() {

        System.out.println(STR."repeat => \{new StringBuffer().repeat("*", 10)}");
        //repeat => **********

        var happy = "你开心吗? 😄";
        System.out.println(STR."isEmoji => \{happy.codePoints().anyMatch(Character::isEmoji)}");
        //isEmoji => true

        System.out.println(111);

        Foo foo = new Foo("name ");
        System.out.println(foo);

        List<Integer> integers = List.of(1, 2, 3);
        for (Integer i : integers) {
            System.out.println(i);
        }

        if (1 > 0) {

        }

        var str = "";
        if (str == null) {

        }

        if (str != null) {

        }
    }
}

record Foo(String name) {

}
