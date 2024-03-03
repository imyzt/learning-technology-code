package top.imyzt.learning.redis.lock;


import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.imyzt.learning.redis.lock.common.RedisLock;

/**
 * @author imyzt
 * @date 2024/02/24
 * @description 模拟领域服务
 */
@Service
public class DomainService {

    @Resource
    private RedisLock redisLock;
    @Resource
    private UserService userService;

    public void save(String name) {

        String lockKey = "lock_key:" + name;
        RedisLock.LockContext lockContext = redisLock.tryLock(lockKey, 10000L);
        if (!lockContext.getTryLock()) {
            printLog("没拿到锁");
            return;
        }

        printLog("拿到锁了" + lockKey);
        try {
            userService.save(name);
        } finally {
            redisLock.release(lockContext);
            printLog("释放锁了");
        }
    }

    private void printLog(String log) {
        System.out.println(String.format("[%s]-[%s]-%s", DateUtil.date(), Thread.currentThread().getName(), log));
    }
}
