package top.imyzt.learning.netty.server.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.imyzt.learning.netty.message.req.LoginRequestMessage;
import top.imyzt.learning.netty.message.resp.LoginResponseMessage;
import top.imyzt.learning.netty.server.service.UserServiceFactory;
import top.imyzt.learning.netty.server.session.SessionFactory;

/**
 * 登录消息处理器
 * @author imyzt
 */
@Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
    }
}
