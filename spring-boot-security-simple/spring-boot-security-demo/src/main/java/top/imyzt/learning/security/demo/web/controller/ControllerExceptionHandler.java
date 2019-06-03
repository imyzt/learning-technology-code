package top.imyzt.learning.security.demo.web.controller;

import org.assertj.core.util.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import top.imyzt.learning.security.demo.exception.UserNotExistException;

import java.util.Map;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description ControllerExceptionHandler
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerUserNotExistException(UserNotExistException e) {
        Map<String, Object> hashMap = Maps.newHashMap("id", e.getId());
        hashMap.put("message", e.getMessage());
        return hashMap;
    }
}
