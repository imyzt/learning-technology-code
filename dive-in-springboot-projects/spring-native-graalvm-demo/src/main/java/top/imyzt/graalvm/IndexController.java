package top.imyzt.graalvm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author imyzt
 * @date 2024/03/18
 * @description Index
 */
@RestController
@RequestMapping
public class IndexController {

    @GetMapping
    public Result nowTime() {
        return new Result("hello, graalvm!", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
