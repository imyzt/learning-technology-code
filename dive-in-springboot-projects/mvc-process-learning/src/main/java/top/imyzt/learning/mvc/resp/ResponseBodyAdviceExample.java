package top.imyzt.learning.mvc.resp;


import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 实现ResponseBodyAdvice接口可以在方法的返回值被HttpMessageConverter写入之前自定义包装方法返回值，比如进行统一的返回值处理，
 * 实现ResponseBodyAdvice的接口同样需要使用@ControllerAdvice注册进框架中
 *
 * 应用场景:
 * 1. 增加统一响应结果集对象
 * 2. 将返回值加密
 *
 * @author imyzt
 * @date 2022/04/08
 */
@ControllerAdvice
public class ResponseBodyAdviceExample implements ResponseBodyAdvice<String> {

    /**
     * 自行判断是否支持处理该类型参数
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    /**
     * 方法的返回值被HttpMessageConverter写入之前自定义包装方法返回值
     */
    @Override
    public String beforeBodyWrite(String body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // 加入自定义逻辑
        // 比如:
        // 1. 增加统一响应结果集对象
        // 2. 将返回值加密

        return body;
    }

}
