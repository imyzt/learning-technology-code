package top.imyzt.learning.springbootevents.demos.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.imyzt.learning.springbootevents.demos.entity.User;
import top.imyzt.learning.springbootevents.demos.listener.event.AddUserEvent;

/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
@Component
@Slf4j
public class UserListener {

    @EventListener(AddUserEvent.class)
    public void addHandler(AddUserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件, 发送短信完成 Normal, user={}", user.toString());
    }

    @TransactionalEventListener(value = AddUserEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void addHandlerAfterCommit(AddUserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件, 发送短信完成 AFTER_COMMIT, user={}", user.toString());
    }

    @TransactionalEventListener(value = AddUserEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void addHandlerBeforeCommit(AddUserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件, 发送短信完成 BEFORE_COMMIT, user={}", user.toString());
    }

    @TransactionalEventListener(value = AddUserEvent.class, phase = TransactionPhase.AFTER_COMPLETION)
    public void addHandlerAfterCompletion(AddUserEvent event) {
        User user = event.getUser();
        log.info("接收到添加用户事件, 发送短信完成 AFTER_COMPLETION, user={}", user.toString());
    }

    @TransactionalEventListener(value = AddUserEvent.class, phase = TransactionPhase.AFTER_ROLLBACK)
    public void addHandlerAfterRollback(AddUserEvent event) {
        User user = event.getUser();
        log.info("添加用户事务执行失败, AFTER_ROLLBACK, user={}", user.toString());
    }
}
