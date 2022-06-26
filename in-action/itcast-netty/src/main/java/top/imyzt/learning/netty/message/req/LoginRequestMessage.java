package top.imyzt.learning.netty.message.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.imyzt.learning.netty.message.Message;

/**
 * 登录请求消息对象
 * @author imyzt
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class LoginRequestMessage extends Message {
    private String username;
    private String password;

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
