package top.imyzt.learning.netty.message.resp;

import top.imyzt.learning.netty.message.Message;

public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
