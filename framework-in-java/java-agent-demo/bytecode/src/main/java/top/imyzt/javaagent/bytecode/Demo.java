package top.imyzt.javaagent.bytecode;


/**
 *
 * 1. javac Demo.java
 * 2. hexdump -C Demo.class 或者 javap -v Demo.class > demo-bytecode.txt, 将内容写入到文件中
 *
 * @author imyzt
 * @date 2024/08/06
 * @description 原始Java文件
 */
public class Demo {

    public static void main(String[] args) {
        final Demo demo = new Demo();
        final int incr = demo.incr(5);
        System.out.println(incr);
    }

    public int incr(int i) {
        return i + 1;
    }
}
