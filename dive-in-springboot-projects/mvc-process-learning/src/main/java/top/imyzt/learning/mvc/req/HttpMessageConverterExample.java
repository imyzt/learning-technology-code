package top.imyzt.learning.mvc.req;


import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * HttpMessageConverter -> 用于自定义参数转换逻辑
 *
 * @author imyzt
 * @date 2022/04/08
 * @description 描述信息
 */
public class HttpMessageConverterExample extends MappingJackson2HttpMessageConverter {

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        System.out.println("MappingJackson2HttpMessageConverter read");

        return super.read(type, contextClass, inputMessage);
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        System.out.println("MappingJackson2HttpMessageConverter writeInternal");

        super.writeInternal(object, type, outputMessage);
    }
}
