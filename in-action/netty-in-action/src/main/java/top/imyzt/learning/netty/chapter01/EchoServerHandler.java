package top.imyzt.learning.netty.chapter01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 继承ChannelInboundHandlerAdapter, 标识一个channel-handler可以被多个channel安全的共享
 *
 * 应用程序通过实现或扩展ChannelHandler来挂钩到事件的生命周期, 并且提供自定义的应用程序逻辑.
 * @author imyzt
 * @date 2022/04/17
 * @description Echo server
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息都要调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        // 打印消息
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        // 将接收到的消息写给发送者, 而不断冲刷出站消息
        ctx.writeAndFlush(in);
    }

    /**
     * 通知ChannelInboundHandler最后一次对channelRead的调用时当前批量读取中的最后一条消息
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 将目前在存于ChannelOutboundBuffer中的消息刷到远程节点, 并且关闭该channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间, 有异常抛出时会调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常堆栈
        cause.printStackTrace();
        // 关闭该channel
        ctx.close();
    }
}