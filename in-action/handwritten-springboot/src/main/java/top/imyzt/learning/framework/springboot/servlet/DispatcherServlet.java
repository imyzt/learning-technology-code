package top.imyzt.learning.framework.springboot.servlet;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import top.imyzt.learning.framework.springboot.annotations.*;
import top.imyzt.learning.framework.springboot.exception.BeanNameExistsException;
import top.imyzt.learning.framework.springboot.exception.ParamErrorException;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description MVC核心请求处理器
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * 存放application.properties配置文件
     */
    private static final Properties CONTEXT_CONFIG = new Properties();

    /**
     * 存放所有的scanPackage下的java类全路径
     */
    private static final List<String> CLASS_NAMES = new ArrayList<>();

    /**
     * 核心ioc容器, 存放所有的bean
     */
    private static final Map<String, Object> IOC_MAP = new HashMap<>();

    /**
     * 存放请求处理器
     */
    private static final Map<String, Method> HANDLER_MAPPING = new HashMap<>();

    @Override
    public void init(ServletConfig config) {

        // 1. 加载配置文件
        doLoadConfig(config);

        // 2. 扫描相关的类(class)
        doScanner(CONTEXT_CONFIG.getProperty("scanPackage"));

        // 3. 实例化相关的类, 并且将实例对象缓存到IOC容器中
        doInstance();

        // 4. 完成自动赋值, DI
        doAutowired();

        // 5. 初始化HandlerMapping
        doInitHandlerMapping();

        System.out.println("Spring Framework is Init...");
    }

    private void doInitHandlerMapping() {

        if (IOC_MAP.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : IOC_MAP.entrySet()) {

            Class<?> clazz = entry.getValue().getClass();
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

    private void doAutowired() {

        if (IOC_MAP.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : IOC_MAP.entrySet()) {

            // 找到bean的所有public/private/protected/default的字段
            Field[] allFields = entry.getValue().getClass().getDeclaredFields();

            if (ArrayUtil.isEmpty(allFields)) {
                continue;
            }

            for (Field field : allFields) {

                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }

                Autowired annotation = field.getAnnotation(Autowired.class);

                String beanName = annotation.value().trim();

                if (StrUtil.isBlank(beanName)) {
                    // 如果没有指定, 直接获取字段类型
                    beanName = field.getType().getName();
                }

                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), IOC_MAP.get(beanName));
                    System.out.println("DI field Name=" + field.getName() + ", bean=" + beanName);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void doInstance() {

        if (CLASS_NAMES.isEmpty()) {
            return;
        }

        try {
            for (String className : CLASS_NAMES) {

                Class<?> clazz = Class.forName(className);


                if (clazz.isAnnotationPresent(RestController.class)) {

                    String beanName = toLowerFirstName(clazz.getSimpleName());
                    Object instance = clazz.newInstance();
                    IOC_MAP.put(beanName, instance);
                    System.out.println("ioc BeanName " + beanName);


                } else if (clazz.isAnnotationPresent(Service.class)) {

                    String beanName = toLowerFirstName(clazz.getSimpleName());

                    // 如果自己有取名字
                    Service service = clazz.getAnnotation(Service.class);
                    if (StrUtil.isNotBlank(service.value())) {
                        beanName = service.value();
                    }
                    Object instance = clazz.newInstance();
                    IOC_MAP.put(beanName, instance);
                    System.out.println("ioc BeanName " + beanName);

                    // 如果是接口, 将接口全部丢入容器中

                    for (Class<?> anInterface : clazz.getInterfaces()) {

                        if (IOC_MAP.containsKey(anInterface)) {
                            throw new BeanNameExistsException("the beanName[" + anInterface.getName() + "] is exists!!");
                        }

                        // 如果是接口, 用接口名称(全限定名)作为key
                        IOC_MAP.put(anInterface.getName(), instance);
                        System.out.println("ioc InterfaceBeanName " + anInterface.getName());
                    }


                } else {
                    continue;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private String toLowerFirstName(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {


        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));

        File file = new File(url.getFile());

        for (File listFile : file.listFiles()) {
            if (listFile.isDirectory()) {
                doScanner(scanPackage + "." + listFile.getName());
            } else {

                if (!listFile.getName().endsWith(".class")) {
                    continue;
                }

                String className = scanPackage + "." + listFile.getName().replaceAll(".class", "");
                System.out.println("scanPackage className = " + className);
                CLASS_NAMES.add(className);
            }


        }
    }

    private void doLoadConfig(ServletConfig config) {

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(config.getInitParameter("contextConfigLocation"));

        try {
            CONTEXT_CONFIG.load(is);
        } catch (IOException e) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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

        // 方法所在类名称
        String simpleName = method.getDeclaringClass().getSimpleName();
        // 找到方法所在类的bean
        Object bean = IOC_MAP.get(toLowerFirstName(simpleName));

        // 请求的所有参数
        Map<String, String[]> parameterMap = req.getParameterMap();


        // 方法的实际参数
        Object[] parameterValues = this.getParameterValues(req, resp, parameterMap, method);

        method.invoke(bean, parameterValues);

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