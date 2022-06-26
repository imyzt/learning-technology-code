package top.imyzt.learning.netty.message.req;

import lombok.Data;
import lombok.ToString;
import top.imyzt.learning.netty.message.Message;

@Data
@ToString(callSuper = true)
public class GroupJoinRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupJoinRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
}
