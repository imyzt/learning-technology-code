package top.imyzt.learning.spring.startup.core;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2024/05/21
 * @description 启动耗时5秒
 */
@Slf4j
public class TestABean {

    @SneakyThrows
    public TestABean() {
        log.info("A Bean开始初始化");
        TimeUnit.SECONDS.sleep(5);
        log.info("A Bean初始化完成");
    }
}
