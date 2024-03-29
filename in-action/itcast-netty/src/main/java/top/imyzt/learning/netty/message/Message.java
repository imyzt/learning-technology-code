package top.imyzt.learning.netty.message;

import lombok.Data;
import top.imyzt.learning.netty.message.req.RpcRequestMessage;
import top.imyzt.learning.netty.message.resp.RpcResponseMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息对象
 * @author imyzt
 */
@Data
public abstract class Message implements Serializable {

    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    /**
     * 请求类型 byte 值
     */
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;
    /**
     * 响应类型 byte 值
     */
    public static final int  RPC_MESSAGE_TYPE_RESPONSE = 102;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestMessage, top.imyzt.learning.netty.message.req.LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage, top.imyzt.learning.netty.message.resp.LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage, top.imyzt.learning.netty.message.req.ChatRequestMessage.class);
        messageClasses.put(ChatResponseMessage, top.imyzt.learning.netty.message.resp.ChatResponseMessage.class);
        messageClasses.put(GroupCreateRequestMessage, top.imyzt.learning.netty.message.req.GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage, top.imyzt.learning.netty.message.resp.GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinRequestMessage, top.imyzt.learning.netty.message.req.GroupJoinRequestMessage.class);
        messageClasses.put(GroupJoinResponseMessage, top.imyzt.learning.netty.message.resp.GroupJoinResponseMessage.class);
        messageClasses.put(GroupQuitRequestMessage, top.imyzt.learning.netty.message.req.GroupQuitRequestMessage.class);
        messageClasses.put(GroupQuitResponseMessage, top.imyzt.learning.netty.message.resp.GroupQuitResponseMessage.class);
        messageClasses.put(GroupChatRequestMessage, top.imyzt.learning.netty.message.req.GroupChatRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage, top.imyzt.learning.netty.message.resp.GroupChatResponseMessage.class);
        messageClasses.put(GroupMembersRequestMessage, top.imyzt.learning.netty.message.req.GroupMembersRequestMessage.class);
        messageClasses.put(GroupMembersResponseMessage, top.imyzt.learning.netty.message.resp.GroupMembersResponseMessage.class);
        messageClasses.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequestMessage.class);
        messageClasses.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponseMessage.class);
    }

}
