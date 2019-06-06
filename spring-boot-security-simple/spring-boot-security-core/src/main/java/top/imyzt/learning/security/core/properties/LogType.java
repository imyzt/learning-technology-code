package top.imyzt.learning.security.core.properties;

/**
 * @author imyzt
 * @date 2019/6/6
 * @description 登录类型
 */
public enum  LogType {

    /**
     * HTML访问形式, 需要重定向
     */
    REDIRECT,

    /**
     * 异步AJAX访问形式, 返回JSON
     */
    JSON
}
