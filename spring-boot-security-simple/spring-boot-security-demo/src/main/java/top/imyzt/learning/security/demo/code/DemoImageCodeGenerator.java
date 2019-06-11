package top.imyzt.learning.security.demo.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.imyzt.learning.security.core.validate.code.ImageCode;
import top.imyzt.learning.security.core.validate.code.ValidateCodeGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 通过实现接口自定义验证码生成规则
 */
//@Component("imageCodeGenerator")
@Slf4j
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

    @Override
    public ImageCode createImage(HttpServletRequest request) {
        // 模拟生成接口, 不编写具体代码实现
        log.info("demo 项目的验证码生成规则.");
        return null;
    }
}
