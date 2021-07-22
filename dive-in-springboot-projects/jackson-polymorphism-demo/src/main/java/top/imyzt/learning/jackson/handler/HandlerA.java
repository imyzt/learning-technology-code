package top.imyzt.learning.jackson.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.imyzt.learning.jackson.pojo.dto.BaseParam;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description A处理器
 */
@Component
@Slf4j
public class HandlerA implements IHandler {

    @Override
    public String handler(BaseParam param) {
        log.info("handlerA, param->{}", param);
        return "A";
    }
}