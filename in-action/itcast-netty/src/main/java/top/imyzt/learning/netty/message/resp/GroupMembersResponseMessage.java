package top.imyzt.learning.netty.message.resp;

import lombok.Data;
import lombok.ToString;
import top.imyzt.learning.netty.message.Message;

import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends Message {

    private Set<String> members;

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}
