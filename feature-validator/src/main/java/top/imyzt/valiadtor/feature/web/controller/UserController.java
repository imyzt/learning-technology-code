package top.imyzt.valiadtor.feature.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.valiadtor.feature.pojo.form.UserRegisterForm;

import javax.validation.Valid;

/**
 * @author imyzt
 * @date 2019/07/12
 * @description 用户控制器
 */
@RestController
@Slf4j
public class UserController {

    @PostMapping("register")
    public void register(@Valid @RequestBody UserRegisterForm registerForm) {
        log.info(registerForm.toString());
    }
}
