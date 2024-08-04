package top.imyzt.learning.springbootevents.demos.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.springbootevents.demos.service.UserService;


/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    @PostMapping("/add")
    public String add(String username, String address) {
        return userService.addUser(username, address);
    }
}
