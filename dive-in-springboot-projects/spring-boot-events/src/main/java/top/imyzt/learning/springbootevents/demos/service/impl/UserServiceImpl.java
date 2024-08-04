package top.imyzt.learning.springbootevents.demos.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.springbootevents.demos.entity.User;
import top.imyzt.learning.springbootevents.demos.listener.event.AddUserEvent;
import top.imyzt.learning.springbootevents.demos.mapper.UserMapper;
import top.imyzt.learning.springbootevents.demos.service.UserService;

/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final ApplicationContext applicationContext;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUser(String username, String address) {
        User user = User.builder().username(username).address(address).build();
        log.info("添加用户开始");
        super.save(user);
        log.info("添加用户成功, 发送事件开始");
        applicationContext.publishEvent(new AddUserEvent(this, user));
        log.info("添加用户成功, 发送事件结束");

        // 模拟异常场景
        if (address.equals("shanghai")) {
            throw new RuntimeException("rollback");
        }
        return user.getId();
    }
}
