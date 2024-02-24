package top.imyzt.learning.redis.lock;


import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.imyzt.learning.redis.lock.common.RedisLock;

import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2024/02/24
 * @description 描述信息
 */
@Service
public class DomainService {

    @Resource
    private RedisLock redisLock;
    @Resource
    private UserService userService;

    public void save(String name) {

        String lockKey = "lock_key";
        RedisLock.LockContext lockContext = redisLock.tryLock(lockKey, 3000L);
        if (!lockContext.isLock()) {
            printLog("没拿到锁");
            return;
        }

        printLog("拿到锁了");
        try {
            userService.save(name);
        } finally {
            redisLock.release(lockContext);
            printLog("释放锁了");
        }

        // 模拟事务没提交上, 但锁已经释放了
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
