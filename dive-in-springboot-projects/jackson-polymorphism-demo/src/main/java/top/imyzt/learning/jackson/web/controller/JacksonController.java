package top.imyzt.learning.jackson.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.jackson.handler.IHandler;
import top.imyzt.learning.jackson.pojo.dto.BaseParam;
import top.imyzt.learning.jackson.pojo.dto.ParamDTO;

import javax.annotation.Resource;

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
    public <T extends BaseParam> String jacksonTest(@Validated @RequestBody ParamDTO<T> param) {
        IHandler iHandler = (IHandler) applicationContext.getBean(param.getType().getHandlerClazz());
        return iHandler.handler(param.getParam());
    }

}