package top.imyzt.learning.springbootevents.demos.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.imyzt.learning.springbootevents.demos.entity.User;

/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
public interface UserService extends IService<User> {
    String addUser(String username, String address);
}
