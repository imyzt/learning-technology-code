package top.imyzt.learning.netty.c2;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description EventLoop
 */
@Slf4j
public class TestEventLoop {

    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());

        // 可以执行 IO任务, 普通任务, 定时任务
        NioEventLoopGroup group = new NioEventLoopGroup(2);

        // 可以执行普通任务, 定时任务
        // DefaultEventLoopGroup eventExecutors = new DefaultEventLoopGroup();

        // 执行普通任务
        group.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("execute...");
        });

        // 执行定时任务
        group.scheduleAtFixedRate(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("scheduleAtFixedRate");
        },1, 1, TimeUnit.SECONDS);

    }
}