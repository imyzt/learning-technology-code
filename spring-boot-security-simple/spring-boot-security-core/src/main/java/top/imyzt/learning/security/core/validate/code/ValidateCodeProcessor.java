package top.imyzt.learning.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 校验码处理器, 封装不同校验码的处理逻辑
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建验证码
     * @param request 请求响应对象封装对象
     * @throws Exception exception
     */
    void create(ServletWebRequest request) throws Exception;
}
