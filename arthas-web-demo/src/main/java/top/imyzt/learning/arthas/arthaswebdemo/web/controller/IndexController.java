package top.imyzt.learning.arthas.arthaswebdemo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author imyzt
 * @date 2021/07/10
 * @description 基础控制器
 */
@RestController
@RequestMapping
public class IndexController {

    private static final Random random = new Random();

    @GetMapping("{num}")
    public Result index(@PathVariable Integer num) throws InterruptedException {
        if (num > 3) {
            throw new NullPointerException();
        }
        return getResult(num);
    }

    private Result getResult(int num) throws InterruptedException {
        Thread.sleep(random.nextInt(1000));
        return Result.builder().param(num).returnObj(num * 2).build();
    }


}