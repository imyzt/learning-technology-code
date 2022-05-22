package top.imyzt.learning.netty.c2.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static top.imyzt.learning.netty.c1.TestByteBufferExam.split;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description NIO server
 */
@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // channel绑定selector, 只关注accept事件
        SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
        log.info("sscKey={}",sscKey);
        // 绑定监听端口
        ssc.bind(new InetSocketAddress(8888));

        while (true) {

            // 没有事件发生时, 线程阻塞, 有事件发生恢复运行
            // select在事件未处理时, 不会阻塞, 事件发生后要么处理,要么取消,不能不处理(会进入死循环)
            selector.select();

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {

                SelectionKey selectionKey = iterator.next();
                // 必须移除, selector只负责写入selectedKeys,不负责删除
                iterator.remove();
                log.info("selectionKey={}", selectionKey);

                if (selectionKey.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.info("sc={}", sc);

                    // 将channel绑定的buffer,以附件的形式绑定到selectorKey中
                    // 关注可读事件
                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_WRITE, null);
                    log.info("scKey={}", scKey);

                    // 建立连接后, 向客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 2000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = StandardCharsets.UTF_8.encode(sb.toString());

                    // 返回值代表实际写入内容
                    int write = sc.write(buffer);
                    log.info("start write byte size={}", write);

                    if (buffer.hasRemaining()) {
                        // 关注可写事件 + 之前关注的其他事件
                        scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
                        // 将未写完的内容, 挂载到附件中, 用于下次网卡空余时发送使用
                        selectionKey.attach(buffer);
                        log.info("start write not flush, listener writeable");
                    }
                } else if (selectionKey.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        // 从附件中取出为这个channel分配的buffer
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                        int read = channel.read(buffer);
                        if (read == -1) {
                            // 客户端调用close关闭, 会发送一个read事件, 消息=-1
                            log.error("client close, channel={}", channel);
                            selectionKey.cancel();
                        } else {

                            split(buffer);

                            if (buffer.position() == buffer.limit()) {
                                // 数据长度过大, buffer空间不够, 需要扩容
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                selectionKey.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        // 客户端异常断开, 会发送一个read事件, 消息=-1
                        log.error("client close", e);
                        selectionKey.channel();
                    }
                } else if (selectionKey.isWritable()) {

                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                    int write = channel.write(buffer);
                    log.info("write byte size={}", write);

                    if (!buffer.hasRemaining()) {
                        // 清除buffer
                        selectionKey.attach(null);
                        // 内容发送完毕后, 不需要再关注可写事件
                        selectionKey.interestOps(selectionKey.interestOps() - SelectionKey.OP_WRITE);
                    }
                } else if (selectionKey.isConnectable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    log.info("connecting channel={}", channel);
                }
            }
        }

    }
}