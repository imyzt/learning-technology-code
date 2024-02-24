package top.imyzt.learning.redis.lock.common;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2024/02/24
 * @description 分布式锁
 */
@Component
public class RedisLock {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> script;

    @PostConstruct
    public void init() {
        script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("release_lock.lua")));
    }

    public LockContext tryLock(String lockKey, Long expireTime) {
        String lockValue = UUID.randomUUID().toString();
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 3000, TimeUnit.MILLISECONDS);
        return new LockContext(lockKey, lockValue, expireTime, TimeUnit.MILLISECONDS, b);
    }

    public void release(LockContext context) {
        if (context.isLock()) {
            ArrayList<String> keys = new ArrayList<>();
            keys.add(context.getLockKey());
            try {
                stringRedisTemplate.execute(this.script, keys, context.getLockValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class LockContext {

        private String lockKey;

        private String lockValue;

        private Long expireTime;

        private TimeUnit timeUnit;

        private Boolean tryLock;

        public LockContext(String lockKey, String lockValue, Long expireTime, TimeUnit timeUnit, Boolean tryLock) {
            this.lockKey = lockKey;
            this.lockValue = lockValue;
            this.expireTime = expireTime;
            this.timeUnit = timeUnit;
            this.tryLock = tryLock;
        }

        public boolean isLock() {
            return this.tryLock;
        }

        public String getLockKey() {
            return lockKey;
        }

        public void setLockKey(String lockKey) {
            this.lockKey = lockKey;
        }

        public String getLockValue() {
            return lockValue;
        }

        public void setLockValue(String lockValue) {
            this.lockValue = lockValue;
        }

        public Long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Long expireTime) {
            this.expireTime = expireTime;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }
}
