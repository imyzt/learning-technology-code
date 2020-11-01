package top.imyzt.learning.spring.framework.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author imyzt
 * @date 2020/10/31
 * @description 封装URL和Object
 */
public class HandlerMapping  {

    /**
     * 请求url
     */
    private Pattern pattern;

    /**
     * 控制器
     */
    private Object controller;

    /**
     * 请求处理方法
     */
    private Method method;

    public HandlerMapping(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}