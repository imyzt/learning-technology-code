package top.imyzt.learning.security.core.validate.code.sms;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 发送短信验证码根接口, 不同服务商可实现该接口
 */
public interface SmsCodeSender {

    /**
     * 发送短信验证码方法
     * @param mobile 手机号
     * @param code 验证码
     */
    void send(String mobile, String code);
}
