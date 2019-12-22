# Web 应用

## 传统servlet应用

### 依赖
```
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency> 
```


### servlet 组件

- Servlet
    - 实现
        - `@WebServlet`
        - 继承`HttpServlet`
        - 注册
    - URL映射
        - `@WebServlet(urlPatterns = "/my/servlet")`
    - 注册
        - `@ServletComponentScan(basePackages = "top.imyzt.study.springboot.web.servlet")`
- Filter
- Listener


### Servlet注册

#### Servlet注解

- `@ServletComponentScan+`  
    - `@WebServlet`
    - `@WebFilter`
    - `@WebListener`

#### Spring Bean

- `@Bean`+
    - Servlet
    - Filter
    - Listener


#### RegistrationBean

- `ServletRegistrationBean`
- `FilterRegistrationBean`
- `ServletListenerRegistrationBean`

### 异步非阻塞

#### 异步Servlet
- `javax.servlet.ServletRequest#startAsync()`
- `javax.servlet.AsyncContext`

#### 非阻塞Servlet 

- `javax.servlet.ServletInputStream#setReadListener`
    - `javax.servlet.ReadListener`
- `javax.servlet.ServletOutputStream#setWriteListener`
    - `javax.servlet.WriteListener`

