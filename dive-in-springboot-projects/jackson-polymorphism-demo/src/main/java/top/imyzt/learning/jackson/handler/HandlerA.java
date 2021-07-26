package top.imyzt.learning.jackson.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.imyzt.learning.jackson.pojo.dto.BaseParam;
import top.imyzt.learning.jackson.pojo.vo.BaseVO;
import top.imyzt.learning.jackson.pojo.dto.ParamA;
import top.imyzt.learning.jackson.pojo.vo.VOB;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description A处理器
 */
@Component
@Slf4j
public class HandlerA implements IHandler {

    @Override
    public String handler(BaseParam paramA) {
        ParamA param = (ParamA) paramA;
        log.info("handlerA, param->{}", param);
        return "A";
    }

    @Override
    public BaseVO getData() {
        VOB vob = new VOB();
        vob.setB("aaa");
        vob.setBaseInfo("baseA");
        return vob;
    }
}