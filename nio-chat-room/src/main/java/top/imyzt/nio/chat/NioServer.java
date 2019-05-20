package top.imyzt.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author imyzt
 * @date 2019/5/18
 * @description NIO 服务端
 */
public class NioServer {

    /**
     * 启动方法
     */
    public void start() throws IOException {

        // 1. 创建Selector
        Selector selector = Selector.open();

        // 2. 根据ServerSocketChannel创建channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3. 为channel绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9000));

        // 4. 设置channel为非阻塞式IO
        serverSocketChannel.configureBlocking(false);

        // 5. 将channel注册到selector上, 监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功!");

        // 6. 循环等待新接入的连接
        for (;;) {

            // 获取可用的channel数量
            int readyChannels = selector.select();

            // 防止空轮询, 导致CPU100%问题
            if (readyChannels == 0) {
                continue;
            }

            // 获取可用的channel集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {

                // selectionKey实例
                SelectionKey selectionKey = iterator.next();

                // 移除Set中当前的selectionKey
                iterator.remove();

                // 7. 根据就绪状态, 调用对应的处理业务逻辑

                // 如果是 接入事件
                if (selectionKey.isAcceptable()) {
                    acceptHandler(serverSocketChannel, selector);
                }

                // 如果是 可读事件
                if (selectionKey.isReadable()) {
                    readHandler(selectionKey, selector);
                }
            }
        }
    }

    /**
     * 接入事件处理器
     */
    private void acceptHandler(ServerSocketChannel serverSocketChannel,
                               Selector selector)
            throws IOException {

        // 如果要是接入事件, 创建SocketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 将SocketChannel设置为非阻塞状态
        socketChannel.configureBlocking(false);

        // 将SocketChannel注册到selector中
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 回复客户端信息
        socketChannel.write(Charset.forName("UTF-8")
                .encode("你与聊天室里其他成员都不是朋友关系, 请注意隐私安全"));
    }

    /**
     * 可读事件处理器
     */
    private void readHandler(SelectionKey selectionKey,
                             Selector selector)
            throws IOException {
        // 要从 SelectorKey 中获取到已经就绪的 channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        // 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 循环读取客户端信息
        StringBuilder request = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {

            // 切换为 读模式
            byteBuffer.flip();

            // 读取buffer中的内容
            request.append(Charset.forName("UTF-8").decode(byteBuffer));
        }

        // 将channel再次注册到selector上, 监听他的可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 将客户端发送的请求信息广播到其他客户端
        if (request.length() > 0) {
            broadCast(selector, socketChannel, request.toString());
        }
    }

    /**
     * 广播给其它客户端
     */
    private void broadCast(Selector selector, SocketChannel sourceChannel, String request) {
        // 获取到所有已接入的客户端的channel
        Set<SelectionKey> selectionKeys = selector.keys();
        selectionKeys.forEach(selectionKey -> {
            SelectableChannel targetChannel = selectionKey.channel();

            // 剔除发消息的客户端(自己)
            if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel) {
                try {
                    // 将消息发送到targetChannel客户端
                    ((SocketChannel) targetChannel).write(Charset.forName("UTF-8").encode(request));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 主方法
     * @param args 启动参数
     */
    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }
}
