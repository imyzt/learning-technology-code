package top.imyzt.nio.chat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author imyzt
 * @date 2019/5/20
 * @description 客户端线程类, 专门接收服务器端响应信息
 */
public class NioClientHandler implements Runnable {

    private Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            for (;;) {

                // 获取可用的channel数量
                int readyChannels = selector.select();

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

                    // 如果是 可读事件
                    if (selectionKey.isReadable()) {
                        readHandler(selectionKey, selector);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // 循环读取服务器端信息
        StringBuilder response = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {

            // 切换为 读模式
            byteBuffer.flip();

            // 读取buffer中的内容
            response.append(Charset.forName("UTF-8").decode(byteBuffer));
        }

        // 将channel再次注册到selector上, 监听他的可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 打印服务器端响应的内容
        if (response.length() > 0) {
            System.out.println(response);
        }
    }
}
