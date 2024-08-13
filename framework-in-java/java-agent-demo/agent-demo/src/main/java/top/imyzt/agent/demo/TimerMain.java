package top.imyzt.agent.demo;


import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import top.imyzt.agent.javaagent.Timer;

import java.io.IOException;

/**
 * @author imyzt
 * @date 2024/08/12
 * @description 目标增强类, 标注{@link Timer}的方法将被增强, 打印方法耗时
 */
public class TimerMain {

    public static void main(String[] args) throws InterruptedException, AgentLoadException, IOException, AgentInitializationException, AttachNotSupportedException {
        System.out.println("timer main demo is running...");

        demo1();
        demo2();

        // 获取当前系统中所有 运行中的 虚拟机
        // List<VirtualMachineDescriptor> list = VirtualMachine.list();
        // for (VirtualMachineDescriptor vm : list) {
        //     if (vm.displayName().endsWith("top.imyzt.agent.demo.TimerMain")) {
        //         VirtualMachine virtualMachine = VirtualMachine.attach(vm.id());
        //         String agentAddress = "";
        //         virtualMachine.loadAgent(agentAddress);
        //         virtualMachine.detach();
        //     }
        // }
    }


    private static void demo1() throws InterruptedException {
        System.out.println("demo1 is start");
        Thread.sleep(1000);
        System.out.println("demo1 is finishing");
    }

    @Timer
    private static void demo2() throws InterruptedException {
        System.out.println("demo2 is start");
        Thread.sleep(2000);
        System.out.println("demo2 is finishing");
    }
}
