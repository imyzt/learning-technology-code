package top.imyzt.agent.javaagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import top.imyzt.agent.proxy.UserServiceAgent;

import java.lang.instrument.Instrumentation;

/**
 * 课程: <a href="https://www.cnblogs.com/yuarvin/p/16847117.html">博客园</a>
 *
 * java -javaagent:~/java-agent-demo/agent-helper/target/agent-helper-1.0-SNAPSHOT-full.jar=k1=v1,k2=v2 -jar ~/java-agent-demo/agent-demo/target/agent-demo-1.0-SNAPSHOT.jar
 *
 * 输出:
 * 我的agent程序跑起来啦!
 * 收到的agent参数是:k1=v1,k2=v2
 * premain方法执行完成了
 * timer main demo is running...
 * demo1 is start
 * demo1 is finishing
 * demo2 is start
 * demo2 is finishing
 * private static void top.imyzt.agent.demo.TimerMain.demo2() throws java.lang.InterruptedException 方法耗时： 2002ms
 *
 *
 * 执行顺序:
 * JVM启动前执行:
 * 有 premain(String args, Instrumentation instrumentation) 则先执行,否则执行 premain(String args)
 * JVM启动后执行:
 * agentmain(String agentArgs, Instrumentation instrumentation)
 *
 * @author imyzt
 * @date 2024/8/12
 * @description Timer注解的字节码增强类
 */
public class TimerAgent {

    /**
     * 基于字节码操作修改目标类字节码, 不用生成子类(当然也可以生成子类, 见{@link UserServiceAgent})
     *
     * JVM 会优先加载 带 Instrumentation 签名的方法，加载成功忽略第二种，如果第一种没有，则加载第二种方法 {@link #premain(String)}
     */
    public static void premain(String args, Instrumentation instrumentation) {
        // 调用 Instrumentation#addTransformer(java.lang.instrument.ClassFileTransformer)
        // 添加对应的字节码文件转换器，用来对字节码进行转换
        // instrumentation.addTransformer(new LogClassFileTransformer());

        System.out.println("我的agent程序跑起来啦!");
        System.out.println("收到的agent参数是:" + args);

        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
            //构建拦截规则
            return builder
                    //method()指定哪些方法需要被拦截，ElementMatchers.any()表示拦截所有方法
                    .method(ElementMatchers.isAnnotatedWith(Timer.class))
                    //intercept()指定拦截上述方法的拦截器
                    .intercept(MethodDelegation.to(TimerInterceptor.class));
        };

        // 监听处理器, 可以拦截字节码增强工具本身的异常
        AgentBuilder.Listener listener = getListener();

        //采用Byte Buddy的AgentBuilder结合Java Agent处理程序
        new AgentBuilder
                //采用ByteBuddy作为默认的Agent实例
                .Default()
                //拦截匹配方式：类以com.itheima.driver开始（其实就是com.itheima.driver包下的所有类）
                .type(ElementMatchers.nameStartsWith("top.imyzt.agent.demo"))
                //拦截到的类由transformer处理
                .transform(transformer)
                .with(listener)
                //安装到 Instrumentation
                .installOn(instrumentation);
        System.out.println("premain方法执行完成了");

    }

    public static void premain(String agentArgs) {

    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        // 同 premain 方法实现
        // instrumentation.addTransformer(new LogClassFileTransformer());
    }


    private static AgentBuilder.Listener getListener() {
        return new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
                throwable.printStackTrace();
                System.out.println("onError");
            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
            }
        };
    }

}
