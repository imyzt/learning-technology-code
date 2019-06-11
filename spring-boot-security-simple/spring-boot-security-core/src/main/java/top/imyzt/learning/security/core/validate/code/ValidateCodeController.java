package top.imyzt.learning.security.core.validate.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author imyzt
 * @date 2019/6/10
 * @description 验证码控制器
 */
@RestController
@Slf4j
public class ValidateCodeController {

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    public final static String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    /**
     * 创建验证码, 根据验证码类型不同, 调用不同的 {@link ValidateCodeProcessor} 接口实现
     */
    @GetMapping("/code/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type)
            throws Exception {
        validateCodeProcessors.get(type + "CodeProcessor").create(new ServletWebRequest(request, response));
    }
}
