package top.imyzt.learning.security.core.validate.code.sms;

import lombok.extern.slf4j.Slf4j;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 默认短信发送实现
 */
@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        log.info("sender sms, mobile = {}, code = {}", mobile, code);
    }
}
