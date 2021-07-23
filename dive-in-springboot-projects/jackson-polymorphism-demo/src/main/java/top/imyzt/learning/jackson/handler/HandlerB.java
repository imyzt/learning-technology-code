package top.imyzt.learning.jackson.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.imyzt.learning.jackson.pojo.dto.BaseParam;
import top.imyzt.learning.jackson.pojo.dto.ParamB;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description B处理器
 */
@Component
@Slf4j
public class HandlerB implements IHandler<ParamB> {

    @Override
    public String handler(ParamB param) {
        log.info("handlerB, param->{}", param);
        return "B";
    }
}