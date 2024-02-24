package top.imyzt.learning.redis.lock;


import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.redis.lock.common.RedisLock;
import top.imyzt.learning.redis.lock.model.User;
import top.imyzt.learning.redis.lock.repository.UserRepository;

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

        User user = userRepository.findUserByName(name);
        if (user != null) {
            printLog("已经写入, 不再写入");
            return;
        }

        // 业务保存
        User save = userRepository.save(new User(name));
        printLog("写入成功, id=" + save.getId());

        // 减慢事务提交过程
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 事务内获取分布式锁
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(String name) {

        String lockKey = "lock_key";
        RedisLock.LockContext lockContext = redisLock.tryLock(lockKey, 3000L);
        if (!lockContext.isLock()) {
            printLog("没拿到锁");
            return;
        }

        printLog("拿到锁了");
        User user = userRepository.findUserByName(name);
        if (user != null) {
            printLog("已经写入, 不再写入");
            return;
        }
        try {
            // 业务保存
            User save = userRepository.save(new User(name));
            printLog("写入成功, id=" + save.getId());
        } finally {
            redisLock.release(lockContext);
            printLog("释放锁了");
        }

        // 模拟事务没提交上
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void printLog(String log) {
        System.out.println(String.format("[%s]-[%s]-%s", DateUtil.date(), Thread.currentThread().getName(), log));
    }
}
