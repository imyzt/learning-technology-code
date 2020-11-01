package top.imyzt.learning.spring.framework.webmvc.servlet;

import cn.hutool.core.util.StrUtil;
import top.imyzt.learning.spring.framework.annotations.RequestMapping;
import top.imyzt.learning.spring.framework.annotations.RestController;
import top.imyzt.learning.spring.framework.context.ApplicationContext;
import top.imyzt.learning.spring.framework.exception.ParamErrorException;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description MVC核心请求处理器
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * 存放请求处理器
     */
    private static final List<HandlerMapping> HANDLER_MAPPING  = new ArrayList<>();
    private static final Map<HandlerMapping, HandlerAdapter> handlerAdapters  = new HashMap<>();
    private List<ViewResolver> viewResolvers = new ArrayList<>();


    private ApplicationContext context;

    @Override
    public void init(ServletConfig config) {

        // IOC 和 DI
        context = new ApplicationContext(config.getInitParameter("contextConfigLocation"));

        // MVC
        initStrategies(context);

        System.out.println("Spring Framework is Init...");
    }

    private void initStrategies(ApplicationContext context) {

        // HandlerMapping
        initHandlerMappings(context);

        // 初始化参数适配器
        initHandlerAdapters(context);

        // 初始化视图转换器
        initViewResolvers(context);

    }

    private void initViewResolvers(ApplicationContext context) {

        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File file = new File(templateRootPath);

        for (File listFile : file.listFiles()) {
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }

    }

    private void initHandlerAdapters(ApplicationContext context) {

        // 将HandlerMapping和HandlerAdapter建立一一对应的关系
        for (HandlerMapping handlerMapping : HANDLER_MAPPING) {
            handlerAdapters.put(handlerMapping, new HandlerAdapter());
        }
    }

    private void initHandlerMappings(ApplicationContext context) {

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

                String regex = ("/" + baseUrl + "/" + requestMapping.value())
                        .replaceAll("\\*", ".*")
                        .replaceAll("/+", "/");

                Pattern pattern = Pattern.compile(regex);
                HANDLER_MAPPING.add(new HandlerMapping(pattern, instance, method));

                System.out.println("Mapped: " + regex  + ", " + method);
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
            //doDispatch(req, resp);
            doDispatchV2(req, resp);
        } catch (ParamErrorException e) {
            String message = e.getMessage();
            resp.getWriter().write("400 Bad Request: \n" + message);
        } catch (Exception e) {
            Map<String, Object> modal = new HashMap<>();
            modal.put("message", StrUtil.isNotBlank(e.getMessage()) ? e.getMessage() : "出错了请联系管理员!");
            modal.put("stackTrace", "Arrays.toString(e.getStackTrace())");
            processDispatchResult(req, resp, new ModalAndView("500", modal));
        }

    }

    private void doDispatchV2(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException, IOException {

        // 1. 根据请求获取HandlerMapping
        HandlerMapping mappedHandler = getHandler(req);

        if (mappedHandler == null) {
            // 没找到, 返回404
            processDispatchResult(req, resp, new ModalAndView("404"));
            return;
        }

        // 2. 根据HandlerMapping找到HandlerAdapter
        HandlerAdapter handlerAdapter = getHandlerAdapter(mappedHandler);

        // 3. 根据HandlerAdapter的方法动态匹配参数, 并且获得返回的ModalAndView
        ModalAndView mv = handlerAdapter.handle(req, resp, mappedHandler);

        // 4. 根据ModalAndView选择使用哪一个ViewResolver进行渲染和解析
        processDispatchResult(req, resp, mv);


    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModalAndView mv) throws IOException {

        if (mv == null) {
            return;
        }

        if (viewResolvers.isEmpty()) {
            return;
        }

        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolverViewName(mv.getViewName());
            view.reader(req, resp, mv.getModal());
            return;
        }

    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping mappedHandler) {
        if (handlerAdapters.isEmpty()) {
            return null;
        }
        return handlerAdapters.get(mappedHandler);
    }

    private HandlerMapping getHandler(HttpServletRequest req) {

        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replaceAll(contextPath, "").replaceAll("/+", "/");

        for (HandlerMapping handlerMapping : HANDLER_MAPPING) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handlerMapping;
        }

        return null;
    }
}