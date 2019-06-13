package top.imyzt.learning.security.core.validate.code.sms;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import top.imyzt.learning.security.core.validate.code.ValidateCode;
import top.imyzt.learning.security.core.validate.code.impl.AbstractValidateCodeProcessor;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 短信验证码发送处理器
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    @Resource
    private SmsCodeSender smsCodeSender;

    /**
     * 短信验证码发送处方法
     * @param request 请求响应对象封装对象
     * @param validateCode 验证码对象
     * @throws ServletRequestBindingException ServletRequestBindingException
     */
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws ServletRequestBindingException {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}
