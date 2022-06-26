package top.imyzt.learning.netty.message.resp;

import lombok.Data;
import lombok.ToString;
import top.imyzt.learning.netty.message.Message;

@Data
@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message {
    private boolean success;
    private String reason;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}
