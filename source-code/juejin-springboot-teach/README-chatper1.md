
# @SpringBootApplication

SpringBoot 主引导程序为根目录标记了`@SpringBootApplication`的类, 例如:  
```java
@SpringBootApplication
public class SpringBootTeachApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTeachApplication.class, args);
    }
}
```
当注释掉`@SpringBootApplication`后, 启动时会报错
```
org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.context.ApplicationContextException: Unable to start ServletWebServerApplicationContext due to missing ServletWebServerFactory bean.
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:161) ~[spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:545) ~[spring-context-5.2.9.RELEASE.jar:5.2.9.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143) ~[spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:758) [spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:750) [spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:397) [spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) [spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1237) [spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1226) [spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at top.imyzt.learing.sourcecode.springboot.SpringBootTeachApplication.main(SpringBootTeachApplication.java:13) [classes/:na]
Caused by: org.springframework.context.ApplicationContextException: Unable to start ServletWebServerApplicationContext due to missing ServletWebServerFactory bean.
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.getWebServerFactory(ServletWebServerApplicationContext.java:205) ~[spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer(ServletWebServerApplicationContext.java:177) ~[spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.onRefresh(ServletWebServerApplicationContext.java:158) ~[spring-boot-2.3.4.RELEASE.jar:2.3.4.RELEASE]
	... 9 common frames omitted

```

所以`SpringApplication.run()`方法被传入的类需要标记`@SpringBootApplication`方法, 为什么需要标注该注解, 我们需要从该注解下手了解整个SpringBoot  

