package top.imyzt.learning.nio.c3;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description 同步非阻塞 多路复用
 */
@Slf4j
public class MultiThreadServer {

    public static void main(String[] args) throws IOException {

        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(9999));

        Worker[] workers = new Worker[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        AtomicInteger index = new AtomicInteger(0);

        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    log.info("connected...{}", sc.getRemoteAddress());
                    // 关联selector
                    log.info("before register...{}", sc.getRemoteAddress());
                    Worker worker = workers[index.getAndIncrement() % workers.length];
                    worker.register(sc);
                    log.info("after register...{}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {

        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean isOpen = false;
        private ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(16);

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            if (!isOpen) {
                thread = new Thread(this, name);
                selector = Selector.open();
                thread.start();
                isOpen = true;
            }
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup();
        }

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                selector.select();

                Runnable task = queue.poll();
                if (task != null) {
                    task.run();
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                        SocketChannel sc = (SocketChannel) key.channel();
                        int read = sc.read(byteBuffer);
                        log.info("read...{}", sc.getRemoteAddress());
                        if (read == -1) {
                            // 客户端调用close关闭, 会发送一个read事件, 消息=-1
                            log.error("client close, channel={}", sc);
                            key.cancel();
                        } else {
                            byteBuffer.flip();
                            log.info("content...{}", StandardCharsets.UTF_8.decode(byteBuffer));
                        }
                    }
                }
            }
        }
    }

}