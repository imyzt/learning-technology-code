package top.imyzt.learning.spring.startup.core;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2024/05/21
 * @description 启动耗时6秒
 */
@Slf4j
public class TestBBean {

    @SneakyThrows
    public TestBBean() {
        log.info("B Bean开始初始化");
        TimeUnit.SECONDS.sleep(6);
        log.info("B Bean初始化完成");
    }
}
