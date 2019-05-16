# 自述文件

首先, 这是一个可以运行起来的项目, 运行它和运行一个普通的SpringBoot项目一样, 你只需要找到启动程序(这一步大部分IDE会主动完成)类, 然后右键`main`方法, 点击`run`即可. 它将会监听你的8080端口.  

> tips: 你需要为你的IDE安装[Lombok](https://www.projectlombok.org/setup/overview), 如果你已经安装, 请忽略这条提醒.

但是, 这不是一个完整的项目, 它只是笔者在MVC开发模式前提下, 在众多项目的开发过程中总结出来的, **针对MVC项目的**异常处理规范, 它从一个上帝模式的角度, 站在后续维护者一边, 
针对我们常用的`throw new Exception`定义了一个强硬的, 不可协商的使用规定.  


# 关于项目

在我们的日常开发中, 异常处理一直是一个特别麻烦而尤为重要的部分, 项目中优雅的异常处理能够带给用户很好的体验, 
也能帮助开发运维人员快速定位排查问题, 这篇blog就是的目的就是将我日常开发中的异常处理方式以分享的形式记录下来. 


# 正文开始

## 先定义一些基础类库

#### ErrorMsg(enum)
通过名称可以知道, 这是一个错误信息的对象, 它使用Java的枚举实现, 包含`code`和`message`属性. 用于反馈前台用户友好的信息提示.在我们的系统中至关重要, 它应该是单一的,详细的. 利用`code`属性, 所有的异常情况使用`500`开始, 使用后三位标识详细的异常代码. 比如`500201`, 它的`message`是文件未找到. 如果ErrorMsg#FILE_NOT_FOUND对象响应到前台, 前端代码可以根据`500`判断该请求处理失败了, 根据`2`判断问题发生在IO操作, 然后根据项目的具体规则, 判断是否将`message`中的信息显示给用户.   

代码块 -- `ErrorMsg`类信息
```java
/**
 * @author imyzt
 * @date 2019/5/8
 * @description 错误信息枚举类
 */
@Getter
@AllArgsConstructor
public enum  ErrorMsg {

    /**
     * 一般情况下禁止使用. 由全局异常拦截器无法识别的异常时使用
     */
    ERROR(500, "服务器错误"),

    /*  ---------------------------- 500100-500199 代表业务类异常  ---------------------------- */

    /**
     * 用户名密码错误
     */
    USERNAME_PASSWORD_ERROR(500101, "用户名密码错误"),
    /**
     * 需要登录
     */
    NEED_LOGIN(500102, "需要登录"),

    OBJECT_UNDEFINED(500103, "对象未定义"),


    /*  ---------------------------- 500200-500299 代表IO类异常  ---------------------------- */
    /**
     * 文件未找到
     */
    FILE_NOT_FOUND(500201, "文件未找到"),


    /* ---------------------------- 500300-500399 代表HTTP类异常  ---------------------------- */
    /**
     * 不支持的请求方法
     */
    UNSUPPORTED_REQUEST_METHOD(500301, "不支持的请求方法"),
    /**
     * 错误的请求参数
     */
    MISSING_REQUEST_PARAMETER(500302, "错误的请求参数"),

    ;

    private Integer code;
    private String message;
}
```

#### Response
该对象不在本次异常处理的讨论范畴, 此处作为一个推荐写在这里.   
此类的作用在于服务器响应前台信息, 在出现问题时响应ErrorMsg给前台, 在操作成功后响应SuccessMsg提示信息给前台.  
只能通过提供的静态方法构造该对象, 防止各种稀奇古怪的内容响应到前台破坏后面的设计.  


代码块 -- `Response`类代码
```java
/**
 * @author imyzt
 * @date 2019/5/8
 * @description 服务器响应对象. 不能单独创建. 只能通过提供的静态方法创建. <br/>
 * 使用必须传递 {@link ErrorMsg }或 {@link SuccessMsg } 对象
 */
@Getter
public class Response<T> {

    private Integer code;
    private String message;
    private T data;

    private Response () {}

    private static class StaticClassSingletonHolder {
        private static final Response INSTANCE = new Response();
    }

    private static Response instance = StaticClassSingletonHolder.INSTANCE;

    public static Response success() {
        instance.code = SuccessMsg.SUCCESS.getCode();
        instance.message = SuccessMsg.SUCCESS.getMessage();
        return instance;
    }

    public static Response success(SuccessMsg successMsg) {
        instance.code = successMsg.getCode();
        instance.message = successMsg.getMessage();
        return instance;
    }

    public static <T> Response success(T data) {
        instance.code = SuccessMsg.SUCCESS.getCode();
        instance.message = SuccessMsg.SUCCESS.getMessage();
        instance.data = data;
        return instance;
    }

    public static <T> Response success(SuccessMsg successMsg, T data) {
        instance.code = successMsg.getCode();
        instance.message = successMsg.getMessage();
        instance.data = data;
        return instance;
    }

    public static Response error(ErrorMsg errorMsg) {
        instance.code = errorMsg.getCode();
        instance.message = errorMsg.getMessage();
        return instance;
    }
}
```

#### SuccessMsg
此类不做过多解释, 也不需要做代码展示, 直接查看代码仓库即可, 就是一个操作成功之后对用户的友好提示.  
eg: 密码修改成功/登录成功

  

## Java Exception

我们都知道Java的所有异常都是继承自`java.lang.Exception`类, 分为编译时异常(CheckException)和运行时异常(RuntimeException).   
- 编译时异常是指在程序编译器需要显式的通过try-catch处理的异常, 比如FileNotFoundException, Exception类中除了RuntimeException之外, 都是checkException.  
- 运行时异常是指在程序编译器无法判断的, 在程序时可能出现的异常, 是不需要我们显式处理的异常, 也正是本文讨论的范畴.   

#### AbstractException
我们可以通过继承`java.lang.RuntimeException`来实现我们自定义的异常, 此处, 我**推荐**所有的项目都编写一个`AbstractException`继承自`RuntimeException`,其它的异常类通过继承该类来保证系统异常的统一管理.   
当然, 这个抽象父类的设计也十分重要, 我只提供了两个默认访问权限的构造方法.  
1. AbstractDemoException(ErrorMsg errorMsg)  
    在大多数时候, 我们要使用的也只是这个构造方法, 将已知的错误信息传入构造一个异常对象, 然后通过`throw new`抛出.  
2. AbstractDemoException(ErrorMsg errorMsg, Throwable cause)  
	这个构造器相当于对上面构造器的补充, 我设想的用法是在某些情况下, 需要`try-catch`一个`CheckException`时, 第一个参数传入我们对该异常的描述(ErrorMsg), 第二个参数可以将该异常的原始堆栈信息保存下来.  


代码块 -- 抽象异常类代码
```java
/**
 * @author imyzt
 * @date 2019/5/8
 * @description 异常类的抽象父类.
 */
@Getter
public abstract class AbstractDemoException extends RuntimeException {

    private ErrorMsg errorMsg;

    AbstractDemoException(ErrorMsg errorMsg, Throwable cause) {
        super(errorMsg.getMessage(), cause);
        this.errorMsg = errorMsg;
    }

    AbstractDemoException(ErrorMsg errorMsg) {
        super(errorMsg.getMessage());
        this.errorMsg = errorMsg;
    }
}
```


#### extends AbstractException
我给出了两个通用的异常类, 通过抽象类的设计, 我们可以自定义各种异常子类. 保证系统的健壮性及可维护性.    
1. BusinessException: 业务异常
2. ParameterException: 参数错误
3. AuthenticationException: 认证异常



## Spring MVC 异常处理

#### @ControllerAdvice

Spring MVC 为我们提供了一套完整的全局异常处理API, 我们需要做的是创建一个类, 加上 `@ControllerAdvice` 注解, 此类就会被标识为一个Spring全局异常拦截器.  
点进入 `ControllerAdvice` 注解的内部我们发现, 它使用了`@Component`注解, 标识使用他的类归属SpringBean管理.  
注解具有一个核心属性`basePackages`, 告诉Spring拦截哪些包下面的异常. 对于我们使用者来说, 我们不需要关系它是如何将异常拦截下来的, 只需要关心的是, 我们怎样将它用在我们的项目中.  

代码块 -- `@ControllerAdvice`类代码  
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {
    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] assignableTypes() default {};

    Class<? extends Annotation>[] annotations() default {};
}
```


#### 编写全局异常拦截器

代码块 -- `GlobalExceptionHandler`类代码
```java
/**
 * @author imyzt
 * @date 2019/5/8
 * @description 全局异常拦截处理器, 捕获包括页面异常和AJAX异常
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 异常拦截核心方法
     * @param request 请求域
     * @param response 响应对象
     * @param e 异常对象
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exception(HttpServletRequest request, HttpServletResponse response, Exception e) {

        ErrorMsg errorMsg = ERROR;

        if (e instanceof AbstractDemoException) {
            AbstractDemoException demoException = (AbstractDemoException) e;
            errorMsg = demoException.getErrorMsg();
        } else if (e instanceof MissingServletRequestParameterException) {
            errorMsg = MISSING_REQUEST_PARAMETER;
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            errorMsg = UNSUPPORTED_REQUEST_METHOD;
        } else if (e instanceof NullPointerException) {
            errorMsg = OBJECT_UNDEFINED;
        }
        // 此处尽可能的写常用的异常


        // 打印异常
        printErrorStack(request, errorMsg, e);

        Map<String, Object> errorMap = new HashMap<>(2);
        errorMap.put("code", errorMsg.getCode());
        errorMap.put("message", errorMsg.getMessage());
        // 判断请求方式, 如果是AJAX请求, 调整响应对象的ContentType
        if (HttpKit.isAjax(request) && responseJson(errorMap, response)) {
            return null;
        } else {
            return new ModelAndView("error", errorMap);
        }
    }

    /**
     * 打印异常堆栈信息
     */
    private void printErrorStack(HttpServletRequest request, ErrorMsg errorMsg, Throwable t) {
        String ipAddr = HttpKit.getIpAddr(request);
        // message 可以扩展信息. 便于排查定位
        StringBuilder message = new StringBuilder();
        if (!ERROR.equals(errorMsg)) {
            message.append("errorMsg=").append(errorMsg.getMessage()).append(",");
        }
        message.append("ipAddr=").append(ipAddr);
        log.error(message.toString(), t);
    }

    /**
     * 响应JSON信息给前台
     */
    private boolean responseJson(Map<String, Object> errorMap, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.write(objectMapper.writeValueAsString(errorMap));
            pw.flush();
        } catch (IOException e) {
            log.error("全局异常拦截器响应内容时出错", e);
            return false;
        }
        return true;
    }
}
```

上面的类全限定名是`top.imyzt.exception.handler.GlobalExceptionHandler`, 它通过类注解`@ControllerAdvice`声明了拦截器身份, 通过 `@ExceptionHandler(Exception.class)`声明`exception`方法只处理`Exception.class`以及其子类的异常. 在项目中, 只推荐捕获到此级别即可, Error级别的属于错误, 不应该被拦截, 而是应该通过其它方式及时通知开发人员. 因为它属于非常严重的问题.   


我们主要关注`exception(HttpServletRequest request, HttpServletResponse response, Exception e)`方法, 方法的开始, 先定义一个`top.imyzt.exception.enums.ErrorMsg`用于保存本次拦截下来的的异常信息,它的实例对象是`top.imyzt.exception.enums.ErrorMsg#ERROR`,关于这个枚举, 我们在上面已经解释了该类. 接下来的条件判断, 是需要我们针对具体项目需求进行扩展的.   

第一个条件判断, 判断该异常是否来自develop抛出, 如果是, 强转之后取到定义在顶级异常类的errorMsg对象. 赋值给当前类的errorMsg对象.后面的条件判断理应越多越好,将与项目业务相关的尽可能多的异常进行处理.  

`GlobalExceptionHandler#printErrorStack` 方法主要承担异常信息打印的任务, StringBuilder的实例对象message理应尽可能多的填写与项目业务相关的信息, 便于异常排查.  

接下来通过请求的请求头, 判断请求是AJAX请求还是页面请求, 对不同来源的请求的进行区别处理, 页面请求则将信息传入model对象,返回前台页面. 异步请求则将信息直接以json信息响应前台.   

#### 使用
上面花了这么大气力将异常处理规范化, 如何使用异常类, 可以参考`top.imyzt.exception.web.IndexController`里面的方法.  
还有一个问题是在什么情况下使用什么异常. 这个可以根据项目的具体需求. 此处**只是制定一套关于异常处理的规范**. 不涉及业务处理.  




# 后记

## 不足
我的文笔有限,通篇都是代码,像流水账一样,develop可以直接看代码,无需看这篇文章.  

## 参考
[Spring MVC/Boot 统一异常处理最佳实践](http://www.zhaojun.im/springboot-exception/)  
[Spring MVC 异常处理最佳实践](https://blog.csdn.net/wlwlwlwl015/article/details/50800991)

## 参与
通过提交PR参与完善该项目, 项目所有代码遵循[阿里巴巴P3C](https://github.com/alibaba/p3c)规范开发.

## 协议
[Anti 996 License.](https://github.com/996icu/996.ICU/blob/master/LICENSE)


