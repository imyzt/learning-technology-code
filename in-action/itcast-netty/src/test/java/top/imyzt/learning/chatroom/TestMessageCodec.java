package top.imyzt.learning.chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import top.imyzt.learning.netty.message.req.LoginRequestMessage;
import top.imyzt.learning.netty.protocol.MessageCodec;

/**
 * @author imyzt
 * @date 2022/06/19
 * @description 测试自定义协议消息解析器
 */
@Slf4j
public class TestMessageCodec {

    public static void main(String[] args) throws Exception {

        // 带@Sharable注解的Handler, 可以被多个线程共享
        LoggingHandler loggingHandler = new LoggingHandler();

        EmbeddedChannel channel = new EmbeddedChannel(
                // 粘包/半包 帧解码器
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                loggingHandler,
                // 自定义消息格式
                new MessageCodec()
        );

        // encode
        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("张三", "123");
        // 出站
        channel.writeOutbound(loginRequestMessage);

        // decode
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, loginRequestMessage, buf);

        // 切割为2次传递, 因为有LengthFieldBaseFrameDecoder, 可以解决
        ByteBuf s1 = buf.slice(0, 100);
        ByteBuf s2 = buf.slice(100, buf.readableBytes() - 100);

        // 入站
        s1.retain(); // 引用计数+1
        channel.writeInbound(s1);  // release 1
        channel.writeInbound(s2);
    }
}