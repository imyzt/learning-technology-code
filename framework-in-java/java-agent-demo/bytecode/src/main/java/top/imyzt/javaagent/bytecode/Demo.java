package top.imyzt.javaagent.bytecode;


/**
 *
 * 退回到 /java-agent-demo/bytecode/src/main/java 目录, 因为带包名的java会把包名做目录名处理
 * 1. javac top/imyzt/javaagent/bytecode/Demo.java
 * 2. hexdump -C Demo.class 或者 javap -v Demo.class > demo-bytecode.txt, 将内容写入到文件中
 * 3. 执行(在/java-agent-demo/bytecode/src/main/java目录) java top/imyzt/javaagent/bytecode/Demo
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
