package top.imyzt.learning.redis.lock.redisluadistributedlock;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import top.imyzt.learning.redis.lock.DomainService;
import top.imyzt.learning.redis.lock.UserService;
import top.imyzt.learning.redis.lock.model.User;
import top.imyzt.learning.redis.lock.repository.UserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisLuaDistributedLockApplicationTests {

    @Resource
    private DomainService domainService;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;

    /**
     * 先获取分布式锁, 再操作事务, 事务提交后再释放分布式锁
     */
    @Test
    void success() throws InterruptedException {

        // 模拟多个线程竞争写入, 正常情况下, 3秒内, 只能写入成功一次
        for (int i = 0; i < 10000; i++) {
            CompletableFuture.runAsync(() -> {
                domainService.save("yzt_1");
            });
        }

        TimeUnit.SECONDS.sleep(20);

        List<User> all = userRepository.findAll();
        System.out.println(all);
        Assert.isTrue(all.size() == 1, "并发失败,写入多条数据");

        userRepository.deleteAll();
    }

    /**
     * 事务内获取分布式锁, 可能出现事务还未提交其它并发就进来,造成读视图不一致最终导致数据写入不符合预期
     */
    @Test
    void fail() throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(20);
        // 模拟多个线程竞争写入, 正常情况下, 3秒内, 只能写入成功一次
        for (int i = 0; i < 10000; i++) {
            pool.execute(() -> {
                userService.saveUser("yzt_2");
            });
        }

        TimeUnit.SECONDS.sleep(10);

        List<User> all = userRepository.findAll();
        System.out.println(all);
        Assert.isTrue(all.size() == 1, "并发失败,写入多条数据");
    }

}
