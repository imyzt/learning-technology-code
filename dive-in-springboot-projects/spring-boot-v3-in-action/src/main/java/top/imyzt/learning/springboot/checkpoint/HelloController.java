package top.imyzt.learning.springboot.checkpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author imyzt
 * @date 2023/07/09
 * @description 描述信息
 */
@RestController
@RequestMapping("hello")
public class HelloController {

    @GetMapping
    public String hello() {
        return "hello";
    }
}