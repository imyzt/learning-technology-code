package top.imyzt.exception.handler;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import top.imyzt.exception.enums.ErrorMsg;
import top.imyzt.exception.exception.AbstractDemoException;
import top.imyzt.exception.utils.HttpKit;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static top.imyzt.exception.enums.ErrorMsg.*;

/**
 * @author imyzt
 * @date 2019/5/8
 * @description 全局异常拦截处理器, 捕获包括页面异常和AJAX异常
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 异常拦截核心方法
     * @param request 请求域
     * @param response 响应对象
     * @param e 异常对象
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpServletRequest request, HttpServletResponse response, Exception e) {

        ErrorMsg errorMsg = ERROR;

        if (e instanceof AbstractDemoException) {
            AbstractDemoException demoException = (AbstractDemoException) e;
            errorMsg = demoException.getErrorMsg();
        } else if (e instanceof MissingServletRequestParameterException) {
            errorMsg = MISSING_REQUEST_PARAMETER;
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            errorMsg = UNSUPPORTED_REQUEST_METHOD;
        } else if (e instanceof NullPointerException) {
            errorMsg = OBJECT_UNDEFINED;
        }
        // 此处尽可能的写常用的异常


        // 打印异常
        printErrorStack(request, errorMsg, e);

        return returnResult(request, response, errorMsg);
    }

    /**
     * 表单参数异常处理
     */
    @ExceptionHandler(value = {BindException.class})
    public ModelAndView bindException(BindException exception, HttpServletRequest req, HttpServletResponse resp) {

        BindingResult bindingResult = exception.getBindingResult();

        String objectName = bindingResult.getFieldError().getObjectName();
        String field = bindingResult.getFieldError().getField();
        String defaultMessage = bindingResult.getFieldError().getDefaultMessage();

        log.error("表单参数错误, 请求地址: {}, 参数对象: {}, 错误字段: {}, 错误详情: {}",
                req.getRequestURI(), objectName, field, defaultMessage);
        return returnResult(req, resp, MISSING_REQUEST_PARAMETER);
    }

    /**
     * 打印异常堆栈信息
     */
    private void printErrorStack(HttpServletRequest request, ErrorMsg errorMsg, Throwable t) {
        String ipAddr = HttpKit.getIpAddr(request);
        // message 可以扩展信息. 便于排查定位
        StringBuilder message = new StringBuilder();
        if (!ERROR.equals(errorMsg)) {
            message.append("errorMsg=").append(errorMsg.getMessage()).append(",");
        }
        message.append("ipAddr=").append(ipAddr);
        log.error(message.toString(), t);
    }

    /**
     * 响应JSON信息给前台
     */
    private boolean responseJson(Map<String, Object> errorMap, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.write(objectMapper.writeValueAsString(errorMap));
            pw.flush();
        } catch (IOException e) {
            log.error("全局异常拦截器响应内容时出错", e);
            return false;
        }
        return true;
    }

    /**
     * 返回前台错误信息
     * 如果是AJAX请求返回JSON
     * 如果是表单请求返回ModelAndView
     */
    private ModelAndView returnResult(HttpServletRequest request, HttpServletResponse response, ErrorMsg errorMsg) {
        Map<String, Object> errorMap = new HashMap<>(2);
        errorMap.put("code", errorMsg.getCode());
        errorMap.put("message", errorMsg.getMessage());
        // 判断请求方式, 如果是AJAX请求, 调整响应对象的ContentType
        if (HttpKit.isAjax(request) && responseJson(errorMap, response)) {
            return null;
        } else {
            return new ModelAndView("error", errorMap);
        }
    }
}
