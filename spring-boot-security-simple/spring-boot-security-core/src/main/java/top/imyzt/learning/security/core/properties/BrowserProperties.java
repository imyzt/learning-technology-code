package top.imyzt.learning.security.core.properties;

import lombok.Data;

import static top.imyzt.learning.security.core.properties.SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

/**
 * @author imyzt
 * @date 2019/6/6
 * @description 浏览器应用属性配置
 */
@Data
public class BrowserProperties {

    /**
     * 系统登录页配置, 如未指定则使用browser默认配置的统一登录页
     */
    private String loginPage = DEFAULT_LOGIN_PAGE_URL;

    /**
     * 登录访问类型
     */
    private LogType logType = LogType.JSON;

    /**
     * 记住我的时间(秒)
     * 默认一小时
     */
    private int rememberMeSeconds = 3600;
}
