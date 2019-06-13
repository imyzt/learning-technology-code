package top.imyzt.learning.security.core.validate.code.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import top.imyzt.learning.security.core.validate.code.*;

import java.util.Map;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 验证码处理器的抽象实现
 */
@Slf4j
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
        String type = getValidateCodeType().toString().toLowerCase();
        String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
        }
        return (C) validateCodeGenerator.generate(request.getRequest());
    }

    /**
     * 保存校验码
     * @param request 请求响应对象
     * @param validateCode 验证码
     */
    private void save(ServletWebRequest request, C validateCode) {
        sessionStrategy.setAttribute(request, getSessionKey(), validateCode);
        log.info("save ->session = {}", request.getRequest().getSession().getAttribute(getSessionKey()));
    }

    /**
     * 发送验证码, 短信验证码需要服务商发送, 图形验证码需要response发送, 由子类实现
     * @param request 请求响应对象封装对象
     * @param validateCode 验证码对象
     * @throws Exception Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;


    @Override
    @SuppressWarnings("unchecked")
    public void validate(ServletWebRequest request) {

        ValidateCodeType validateCodeType = getValidateCodeType();
        String sessionKey = getSessionKey();

        log.info("validate -> type = {}, session = {}", validateCodeType, request.getRequest().getSession().getAttribute(sessionKey));

        C codeInSession = (C) sessionStrategy.getAttribute(request, sessionKey);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils
                    .getStringParameter(request.getRequest(), validateCodeType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(validateCodeType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(validateCodeType + "验证码不存在");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException(validateCodeType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(validateCodeType + "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);

    }

    /**
     * 获取对应类型的Session key
     * @return SESSION_KEY_FOR_CODE_IMAGE 或 SESSION_KEY_FOR_CODE_SMS
     */
    private String getSessionKey() {
        return SESSION_KEY_PREFIX + getValidateCodeType().toString().toUpperCase();
    }

    /**
     * 获取请求对应的验证码类型
     * @return (SMS/IMAGE)
     */
    private ValidateCodeType getValidateCodeType() {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

}
