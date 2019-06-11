package top.imyzt.learning.security.core.validate.code.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;
import top.imyzt.learning.security.core.validate.code.ValidateCode;
import top.imyzt.learning.security.core.validate.code.ValidateCodeGenerator;
import top.imyzt.learning.security.core.validate.code.ValidateCodeProcessor;

import java.util.Map;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 验证码处理器的抽象实现
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * Spring启动时会查找所有ValidateCodeGenerator接口的实现, 然后以bean的名称作为key, bean对象作为value
     * 依赖搜索
     */
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;

    @Override
    public void create(ServletWebRequest request) throws Exception {

        // 根据不同请求方式, 生成不同验证码
        C validateCode = generator(request);

        // 都是保存session, 该抽象类统一对外提供
        save(request, validateCode);

        // 发送服务不同, 交由子类实现
        send(request, validateCode);
    }

    /**
     * 生成验证码
     * @param request 请求响应对象
     * @return 验证码
     */
    @SuppressWarnings("unchecked")
    private C generator(ServletWebRequest request) {
        String processorType = getProcessorType(request);
        ValidateCodeGenerator codeGenerator = validateCodeGenerators.get(processorType + "CodeGenerator");
        return (C) codeGenerator.generate(request.getRequest());
    }

    /**
     * 根据请求的URI, 获取验证码的类型
     * @param request 请求响应对象
     * @return 请求类型(image/sms)
     */
    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), "/code/");
    }

    /**
     * 保存校验码
     * @param request 请求响应对象
     * @param validateCode 验证码
     */
    private void save(ServletWebRequest request, C validateCode) {
        sessionStrategy.setAttribute(new ServletWebRequest(request.getRequest()),
                SESSION_KEY_PREFIX + getProcessorType(request).toUpperCase(),
                validateCode);
    }

    /**
     * 发送验证码, 短信验证码需要服务商发送, 图形验证码需要response发送, 由子类实现
     * @param request 请求响应对象封装对象
     * @param validateCode 验证码对象
     * @throws Exception Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;
}
