package top.imyzt.learning.redis.lock;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.redis.lock.common.RedisLock;
import top.imyzt.learning.redis.lock.model.User;
import top.imyzt.learning.redis.lock.repository.UserRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2024/02/24
 * @description 模拟业务服务
 */
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private RedisLock redisLock;

    /**
     * 先获取分布式锁再操作事务
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(String name) {

        List<User> users = userRepository.findUsersByName(name);
        if (CollUtil.isNotEmpty(users)) {
            printLog("已经写入, 不再写入" + users);
            return;
        }

        // 业务保存
        User save = userRepository.save(new User(name));
        printLog("写入成功, id=" + save.getId());

        // 减慢事务提交过程
        try {
            // 模拟执行很慢, 等待watchdog续期
            TimeUnit.SECONDS.sleep(70);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 事务内获取分布式锁
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveUserWithDistributedLock(String name) {

        String lockKey = "lock_key:" + name;
        RedisLock.LockContext lockContext = redisLock.tryLock(lockKey, 10000L);
        if (!lockContext.getTryLock()) {
            // printLog("没拿到锁");
            return;
        }

        printLog("拿到锁了" + lockKey);

        try {
            this.save(name);
        } finally {
            redisLock.release(lockContext);
            printLog("释放锁了");
        }
    }

    private void printLog(String log) {
        System.out.println(String.format("[%s]-[%s]-%s", DateUtil.date(), Thread.currentThread().getName(), log));
    }
}
