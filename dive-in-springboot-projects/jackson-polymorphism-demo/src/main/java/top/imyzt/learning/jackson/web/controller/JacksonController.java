package top.imyzt.learning.jackson.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.jackson.common.enums.ParamEnum;
import top.imyzt.learning.jackson.handler.IHandler;
import top.imyzt.learning.jackson.pojo.dto.ParamDTO;
import top.imyzt.learning.jackson.pojo.vo.BaseVO;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author imyzt
 * @date 2021/07/22
 * @description jackson @class 测试
 */
@Slf4j
@RestController
@RequestMapping
public class JacksonController {

    @Resource
    private ApplicationContext applicationContext;

    @PostMapping
    public String jacksonTest(@Validated @RequestBody ParamDTO param) {
        Class<? extends IHandler> handlerClazz = param.getType().getHandlerClazz();
        IHandler iHandler = applicationContext.getBean(handlerClazz);
        return iHandler.handler(param.getParam());
    }

    @GetMapping
    public BaseVO jacksonGetTest(@NotNull(message = "type不能为空") String type) {
        Class<? extends IHandler> handlerClazz = ParamEnum.get(type).getHandlerClazz();
        IHandler iHandler = applicationContext.getBean(handlerClazz);
        return iHandler.getData();
    }

}