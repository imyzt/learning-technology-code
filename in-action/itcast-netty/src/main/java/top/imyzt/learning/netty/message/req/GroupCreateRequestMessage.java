package top.imyzt.learning.netty.message.req;

import lombok.Data;
import lombok.ToString;
import top.imyzt.learning.netty.message.Message;

import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupCreateRequestMessage extends Message {
    private String groupName;
    private Set<String> members;

    public GroupCreateRequestMessage(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
