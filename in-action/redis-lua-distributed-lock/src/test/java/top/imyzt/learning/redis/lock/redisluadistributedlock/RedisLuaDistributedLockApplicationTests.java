package top.imyzt.learning.redis.lock.redisluadistributedlock;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import top.imyzt.learning.redis.lock.DomainService;
import top.imyzt.learning.redis.lock.model.User;
import top.imyzt.learning.redis.lock.repository.UserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisLuaDistributedLockApplicationTests {

    @Resource
    private DomainService domainService;
    @Resource
    private UserRepository userRepository;

    @Test
    void contextLoads() throws InterruptedException {

        // 模拟多个线程竞争写入, 正常情况下, 3秒内, 只能写入成功一次
        for (int i = 0; i < 10000; i++) {
            CompletableFuture.runAsync(() -> {
                domainService.save("yzt");
            });
        }

        TimeUnit.SECONDS.sleep(10);

        List<User> all = userRepository.findAll();
        Assert.isTrue(all.size() == 1, "并发失败,写入多条数据");
    }

}
