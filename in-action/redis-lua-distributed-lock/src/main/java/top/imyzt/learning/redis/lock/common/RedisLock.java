package top.imyzt.learning.redis.lock.common;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2024/02/24
 * @description 分布式锁
 */
@Component
@Slf4j
public class RedisLock {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final Set<LockContext> LOCK_CONTEXTS = new HashSet<>();

    private DefaultRedisScript<Long> script;

    @PostConstruct
    public void init() {
        script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("release_lock.lua")));

        // watch dog
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (!LOCK_CONTEXTS.isEmpty()) {
                for (LockContext lockContext : LOCK_CONTEXTS) {
                    // 如果执行线程还未释放锁, 续期30s(模拟Redisson)
                    stringRedisTemplate.expire(lockContext.getLockKey(), Duration.ofSeconds(30));
                    Long expire = stringRedisTemplate.getExpire(lockContext.getLockKey());
                    log.info("WatchDog, expire 30s, lockKey={}, ttl={}", lockContext.getLockKey(), expire);
                }
            }
                }, 0,
                // 10秒检测一次
                10, TimeUnit.SECONDS);
    }

    public LockContext tryLock(String lockKey, Long expireTime) {
        String lockValue = UUID.randomUUID().toString();
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.MILLISECONDS);
        LockContext lockContext = new LockContext(lockKey, lockValue, expireTime, TimeUnit.MILLISECONDS, b);
        if (lockContext.getTryLock()) {
            LOCK_CONTEXTS.add(lockContext);
        }
        return lockContext;
    }

    public void release(LockContext context) {
        if (context.getTryLock()) {
            ArrayList<String> keys = new ArrayList<>();
            keys.add(context.getLockKey());
            try {
                stringRedisTemplate.execute(this.script, keys, context.getLockValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOCK_CONTEXTS.remove(context);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LockContext {

        private String lockKey;

        private String lockValue;

        private Long expireTime;

        private TimeUnit timeUnit;

        private Boolean tryLock;
    }
}
