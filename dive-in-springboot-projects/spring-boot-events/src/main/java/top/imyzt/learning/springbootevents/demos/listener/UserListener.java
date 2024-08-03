package top.imyzt.learning.springbootevents.demos.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.imyzt.learning.springbootevents.demos.entity.User;
import top.imyzt.learning.springbootevents.demos.listener.event.UserEvent;

/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
@Component
@Slf4j
public class UserListener {

    @EventListener(UserEvent.class)
    public void addHandler(UserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件 Normal, user={}", user.toString());
    }

    @TransactionalEventListener(value = UserEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void addHandlerAfterCommit(UserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件 AFTER_COMMIT, user={}", user.toString());
    }

    @TransactionalEventListener(value = UserEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void addHandlerBeforeCommit(UserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件 BEFORE_COMMIT, user={}", user.toString());
    }

    @TransactionalEventListener(value = UserEvent.class, phase = TransactionPhase.AFTER_COMPLETION)
    public void addHandlerAfterCompletion(UserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件 AFTER_COMPLETION, user={}", user.toString());
    }

    @TransactionalEventListener(value = UserEvent.class, phase = TransactionPhase.AFTER_ROLLBACK)
    public void addHandlerAfterRollback(UserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件 AFTER_ROLLBACK, user={}", user.toString());
    }
}
