package top.imyzt.learning.redis.lock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.imyzt.learning.redis.lock.model.User;

/**
 * @author imyzt
 * @date 2024/02/24
 * @description 测试数据源
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByName(String name);
}
