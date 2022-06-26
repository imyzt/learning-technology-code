package top.imyzt.learning.netty.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 自定义协议帧解码器, 服务端/客户端可复用
 * 基于自定义协议{@link MessageCodec}实现
 * @author imyzt
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
