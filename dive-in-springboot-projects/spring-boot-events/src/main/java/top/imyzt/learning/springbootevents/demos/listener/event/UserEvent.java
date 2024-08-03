package top.imyzt.learning.springbootevents.demos.listener.event;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import top.imyzt.learning.springbootevents.demos.entity.User;

/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
@Getter
public class UserEvent extends ApplicationEvent {

    private final User user;

    public UserEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
