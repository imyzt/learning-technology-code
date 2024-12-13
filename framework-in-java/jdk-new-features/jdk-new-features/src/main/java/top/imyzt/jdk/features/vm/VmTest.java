package top.imyzt.jdk.features.vm;


import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author imyzt
 * @date 2024/12/13
 * @description jvm 对象信息
 */
public class VmTest {

    public static class Demo {
        private String name;

        public Demo(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        // 查看 jvm 信息
        System.out.println(VM.current().details());
        //# WARNING: Unable to get Instrumentation. Dynamic Attach failed. You may add this JAR as -javaagent manually, or supply -Djdk.attach.allowAttachSelf
        //# WARNING: Unable to attach Serviceability Agent. You can try again with escalated privileges. Two options: a) use -Djol.tryWithSudo=true to try with sudo; b) echo 0 | sudo tee /proc/sys/kernel/yama/ptrace_scope
        //# Running 64-bit HotSpot VM.
        //# Using compressed oop with 3-bit shift.
        //# Using compressed klass with 3-bit shift.
        //# WARNING | Compressed references base/shifts are guessed by the experiment!
        //# WARNING | Therefore, computed addresses are just guesses, and ARE NOT RELIABLE.
        //# WARNING | Make sure to attach Serviceability Agent to get the reliable addresses.
        //# Objects are 8 bytes aligned.
        //# Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
        //# Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]

        // 查看对象的内存布局
        Object obj = new Demo("1");
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        //top.imyzt.jdk.features.vm.VmTest$Demo object internals:
        // OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
        //      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
        //      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
        //      8     4                    (object header)                           e0 e9 01 01 (11100000 11101001 00000001 00000001) (16902624)
        //     12     4   java.lang.String Demo.name                                 (object)
        //Instance size: 16 bytes
        //Space losses: 0 bytes internal + 0 bytes external = 0 bytes total

        // 查看类对象内存布局
        System.out.println(ClassLayout.parseClass(Demo.class).toPrintable());
        //top.imyzt.jdk.features.vm.VmTest$Demo object internals:
        // OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
        //      0    12                    (object header)                           N/A
        //     12     4   java.lang.String Demo.name                                 N/A
        //Instance size: 16 bytes
        //Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
    }
}
