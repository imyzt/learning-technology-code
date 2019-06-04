## 项目模块结构

- spring-boot-security-simple: 父项目, 主模块
- spring-boot-security-core: 核心业务逻辑
- spring-boot-security-browser: 浏览器安全特定代码
- spring-boot-security-app: app相关特定代码
- spring-boot-security-demo: 样例程序

# 学习记录

## Restful API拦截

- 过滤器(implements Filter)  
    优点: 能拿到原始的HTTP请求  
    局限性: 不能获取对应的请求处理器(controller)的信息
    
- 拦截器(implements HandlerInterceptor)   
    优点: 既能拿到原始HTTP请求信息, 也能获取对应的请求处理器(controller)的信息
    局限性: 不能获取对应的请求处理器的**值**(方法入参)
    
- 切片(Aspect)  
    优点: 能拿到请求处理器信息以及请求处理器的**参数值**  
    局限性: 拿不到原始的HTTP请求信息

## 请求的处理过程

正常情况下按照下图流程  
异常情况下, 如果Aspect没有做异常处理, 将会向上抛, ControllerAdvice没有处理, 则一直向上抛. 直至Filter没有处理, 则会抛出给Tomcat展示到前端
![请求处理](https://i.loli.net/2019/06/04/5cf5e0bb91cbe93775.jpg)


## about

这个是学习spring-security的代码汇总, 学习的是慕课视频[Spring Security开发安全的REST服务](https://coding.imooc.com/class/consult/134.html)