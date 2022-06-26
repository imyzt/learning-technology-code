package top.imyzt.learning.netty.message.req;

import top.imyzt.learning.netty.message.Message;

public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
