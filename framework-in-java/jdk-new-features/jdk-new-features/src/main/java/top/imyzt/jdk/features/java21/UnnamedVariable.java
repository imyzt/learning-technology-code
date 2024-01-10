package top.imyzt.jdk.features.java21;


import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2023/12/21
 * @description 像golang学习, 支持不使用的变量不命名了
 */
public class UnnamedVariable {

    void main() {

        List<String> list = new ArrayList<>();
        list.add("first");
        list.add("last");

        try {
            System.out.println(STR."first -> \{list.getFirst()}");
            System.out.println(STR."last -> \{list.getLast()}");
            System.out.println("try");
        } catch (Exception _) {
            System.out.println("异常了, 但是我没有 Exception");
        }
    }
}
