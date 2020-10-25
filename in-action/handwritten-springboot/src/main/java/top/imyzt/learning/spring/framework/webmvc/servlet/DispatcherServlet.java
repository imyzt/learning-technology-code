package top.imyzt.learning.spring.framework.webmvc.servlet;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import top.imyzt.learning.spring.framework.annotations.RequestMapping;
import top.imyzt.learning.spring.framework.annotations.RequestParam;
import top.imyzt.learning.spring.framework.annotations.RestController;
import top.imyzt.learning.spring.framework.context.ApplicationContext;
import top.imyzt.learning.spring.framework.exception.ParamErrorException;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description MVC核心请求处理器
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * 存放请求处理器
     */
    private static final Map<String, Method> HANDLER_MAPPING = new HashMap<>();


    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) {

//        // 1. 加载配置文件
//        doLoadConfig(config);
//
//        // 2. 扫描相关的类(class)
//        doScanner(CONTEXT_CONFIG.getProperty("scanPackage"));
//
//        // 3. 实例化相关的类, 并且将实例对象缓存到IOC容器中
//        doInstance();
//
//        // 4. 完成自动赋值, DI
//        doAutowired();


        context = new ApplicationContext(config.getInitParameter("contextConfigLocation"));

        // 5. 初始化HandlerMapping
        doInitHandlerMapping();

        System.out.println("Spring Framework is Init...");
    }

    private void doInitHandlerMapping() {

        if (this.context.getBeanDefinitionCount() == 0) {
            return;
        }

        String[] beanNames = this.context.getBeanDefinitionNames();
        for (String beanName : beanNames) {

            Object instance = this.context.getBean(beanName);
            Class<?> clazz = instance .getClass();
            if (!clazz.isAnnotationPresent(RestController.class)) {
                continue;
            }

            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }

            for (Method method : clazz.getMethods()) {

                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");

                HANDLER_MAPPING.put(url, method);
                System.out.println("Mapped: " + url + ", " + method);
            }
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 真正负责请求的分发
        try {
            doDispatch(req, resp);
        } catch (ParamErrorException e) {
            String message = e.getMessage();
            resp.getWriter().write("400 Bad Request: \n" + message);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Server Error: \n" + Arrays.toString(e.getStackTrace()));
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {


        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replaceAll(contextPath, "").replaceAll("/+", "/");

        System.out.println("url =---=  " + url);

        Method method = HANDLER_MAPPING.get(url);

        if (Objects.isNull(method)) {
            resp.getWriter().write("404 Not Found!");
            return;
        }

        // 请求的所有参数
        Map<String, String[]> parameterMap = req.getParameterMap();


        // 方法的实际参数
        Object[] parameterValues = this.getParameterValues(req, resp, parameterMap, method);

        method.invoke(this.context.getBean(method.getDeclaringClass()), parameterValues);

    }

    /**
     * 获取方法的实际参数
     */
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