package top.imyzt.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

    // 定义 premain 方法并实现
    public static void premain(String args, Instrumentation instrumentation) {
        // 调用 Instrumentation#addTransformer(java.lang.instrument.ClassFileTransformer)
        // 添加对应的字节码文件转换器，用来对字节码进行转换
        // instrumentation.addTransformer(new LogClassFileTransformer());

        System.out.println("我的agent程序跑起来啦!");
        System.out.println("收到的agent参数是:" + args);
    }

    // 定义 agentmain 方法并实现
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        // 同 premain 方法实现
        // instrumentation.addTransformer(new LogClassFileTransformer());
    }

    // 自定义的字节码文件转换器
    static class LogClassFileTransformer implements ClassFileTransformer {


        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            // 字节码转换的实现逻辑，通过解析原始字节码，进行替换后返回更新后的字节码，达到修改实现的目的
            return null;
        }
    }
}
