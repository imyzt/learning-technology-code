package top.imyzt.learning.security.core.validate.code.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import top.imyzt.learning.security.core.validate.code.impl.AbstractValidateCodeProcessor;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 图形验证码发送处理器
 */
@Component("imageValidateCodeProcessor")
@Slf4j
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    /**
     * 图形验证码发送方法
     * @param request 请求响应对象封装对象
     * @param imageCode 图片验证码对象
     * @throws IOException IOException
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws IOException {
        ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
        log.info("图片验证码 = {}", imageCode.getCode());
    }
}
