package top.imyzt.learning.redis.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisLuaDistributedLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisLuaDistributedLockApplication.class, args);
    }

}
