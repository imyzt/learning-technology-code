package top.imyzt.learning.spring.framework.webmvc.servlet;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import top.imyzt.learning.spring.framework.annotations.RequestParam;
import top.imyzt.learning.spring.framework.annotations.ResponseBody;
import top.imyzt.learning.spring.framework.exception.ParamErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2020/10/31
 * @description 请求适配器
 */
public class HandlerAdapter {
    public ModalAndView handle(HttpServletRequest req, HttpServletResponse resp, HandlerMapping mappedHandler) throws InvocationTargetException, IllegalAccessException, IOException {

        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replaceAll(contextPath, "").replaceAll("/+", "/");

        System.out.println("url =---=  " + url);

        Method method = mappedHandler.getMethod();

        if (Objects.isNull(method)) {
            return null;
        }

        // 请求的所有参数
        Map<String, String[]> parameterMap = req.getParameterMap();

        // 方法的实际参数
        Object[] parameterValues = this.getParameterValues(req, resp, parameterMap, method);

        // 调用方法
        Object result = method.invoke(mappedHandler.getController(), parameterValues);

        if (result == null || result instanceof Void) {
            return null;
        }

        if (mappedHandler.getMethod().getReturnType() == ModalAndView.class) {
            return (ModalAndView) result;
        }

        // 需要序列化为字符串
        if (method.isAnnotationPresent(ResponseBody.class)) {
            String jsonStr = JSON.toJSONString(result);
            resp.getWriter().write(jsonStr);
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("content-type","application/json;charset=UTF-8");
            return null;
        }

        return null;
    }

    private Object[] getParameterValues(HttpServletRequest req, HttpServletResponse resp,
                                        Map<String, String[]> parameterMap, Method method) {

        // 方法的所有形式参数的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        // 方法的所有形式参数的类型
        Class<?>[] parameterTypes = method.getParameterTypes();

        Object[] parameterValues = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            // 每一个参数的类型
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.equals(HttpServletRequest.class)) {
                parameterValues[i] = req;
            } else if (parameterType.equals(HttpServletResponse.class)) {
                parameterValues[i] = resp;
            } else if (parameterType.equals(String.class)) {

                // 拿到当前参数的注解
                Annotation[] currParameterAnnotation = parameterAnnotations[i];
                for (Annotation parameterAnnotation : currParameterAnnotation) {
                    if (parameterAnnotation instanceof RequestParam) {
                        RequestParam requestParam = (RequestParam) parameterAnnotation;
                        String name = requestParam.name();

                        String[] param = parameterMap.get(name);
                        String value = Arrays.toString(param).replaceAll("[\\[\\]]", "")
                                .replaceAll("\\s", "");

                        if (ArrayUtil.isEmpty(param)) {
                            String defaultValue = requestParam.defaultValue();
                            // 如果没有值, 也没有默认值, 但是又需要值, 就报错
                            if (requestParam.required() && StrUtil.isBlank(defaultValue)) {
                                throw new ParamErrorException("param [" + name + "] required not null!");
                            }
                            value = defaultValue;
                        }

                        parameterValues[i] = value;

                    }
                }
            }
        }
        return parameterValues;
    }
}