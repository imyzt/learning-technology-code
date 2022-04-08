package top.imyzt.learning.mvc.req;


import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 实现RequestBodyAdvice接口可以在请求体被HttpMessageConverter转换成Object前、后注入自己的逻辑，
 * 该方法的参数需要是被 @RequestBody修饰或者HttpEntity，实现RequestBodyAdvice接口的类需要使用@ControllerAdvice注册进框架中
 *
 * 也可以继承 {@link RequestBodyAdviceAdapter}, 选择自己需要处理的时机实现方法即可
 *
 * 应用场景:
 * 1. 可以用于请求报文为密文时, 结构体解密为JSON
 *
 * @author imyzt
 * @date 2022/04/08
 */
@ControllerAdvice
public class RequestBodyAdviceExample implements RequestBodyAdvice {

    /**
     * 自行判断是否支持处理该类型参数
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    /**
     * 请求体被HttpMessageConverter转换成Object 前 注入自己逻辑的切入点
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        // 解析字节流 -> 字符串
        String content = new BufferedReader(new InputStreamReader(inputMessage.getBody())).lines().collect(Collectors.joining(System.lineSeparator()));

        // 中间业务处理
        // 比如: AES字符串解密为JSONStr...

        // 将处理后的字符串，构造新的读取流
        InputStream rawInputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        return new HttpInputMessage() {
            @Override @NonNull
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
            @Override @NonNull
            public InputStream getBody() {
                return rawInputStream;
            }
        };

    }

    /**
     * 请求体被HttpMessageConverter转换成Object 后 注入自己逻辑的切入点
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 请求体为空时, 注入自己的逻辑
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
