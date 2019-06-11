package top.imyzt.learning.security.core.validate.code;

import javax.servlet.http.HttpServletRequest;

/**
 * @author imyzt
 * @date 2019/6/11
 * @description 生成验证码行为的抽象接口, 可实现该接口自定义生成行为
 */
public interface ValidateCodeGenerator {

    /**
     * 生成验证码方法
     * @param request 请求
     * @return 图片验证码信息
     */
    ImageCode createImage(HttpServletRequest request);
}
