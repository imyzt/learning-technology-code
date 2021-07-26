package top.imyzt.learning.jackson.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.imyzt.learning.jackson.pojo.dto.BaseParam;
import top.imyzt.learning.jackson.pojo.dto.ParamB;
import top.imyzt.learning.jackson.pojo.vo.VOB;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description B处理器
 */
@Component
@Slf4j
public class HandlerB implements IHandler {

    @Override
    public String handler(BaseParam paramB) {
        ParamB param = (ParamB) paramB;
        log.info("handlerB, param->{}", param);
        return "B";
    }

    @Override
    public VOB getData() {
        VOB vob = new VOB();
        vob.setB("bbb");
        vob.setBaseInfo("baseB");
        return vob;
    }
}