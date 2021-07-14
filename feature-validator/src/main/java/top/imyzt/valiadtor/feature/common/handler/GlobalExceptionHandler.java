package top.imyzt.valiadtor.feature.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author imyzt
 * @date 2019/07/12
 * @description 全局异常拦截器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BindException.class})
    public void exception(BindException exception, HttpServletRequest req) {

            BindingResult bindingResult = exception.getBindingResult();

        String objectName = bindingResult.getFieldError().getObjectName();
        String field = bindingResult.getFieldError().getField();
        String defaultMessage = bindingResult.getFieldError().getDefaultMessage();

        log.error("表单参数错误, 请求地址: {}, 参数对象: {}, 错误字段: {}, 错误详情: {}",
                req.getRequestURI(), objectName, field, defaultMessage);
    }
}
