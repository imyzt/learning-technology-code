# 第一阶段, 简单Spring IOC/DI 和MVC
阶段完成节点 commitId: cb09798d52c87d3eb442d80823b6e5bb5b80a896

## DispatcherServlet核心处理流程

1. 加载配置文件, application.properties
2. 读取scanPackage下所有的javaClass, 存放起来
3. 读取所有javaClass中标注了@Service或@RestController的需要托管的Bean, 通过反射创建instance存放在Bean工厂
4. 读取所有bean中每一个bean的每一个标注了@Autowired的Field, 通过在IOC Bean工厂中查找, 反射设置值完成DI
5. 读取所有bean中标注了@RestController的控制器, 通过读取每一个标注了@RequestMapping的方法, 将其完成URL < - > Method的绑定, 存放在HandlerMapping中
6. doDispatch()方法获取请求URL, 匹配HandlerMapping则通过invoke()反射调用方法处理

## 配置Tomcat启动

1. 新建Tomcat, 选择本地Tomcat目录

![新建Tomcat](http://blog.imyzt.top/upload/2020/10/r35r878q3eh9loh2hhctap5v1i.png)

2. 配置deployment, 将Tomcat webapp目录设置为当前war, contextPath为`/`

![配置deployment](http://blog.imyzt.top/upload/2020/10/3kua1frkv0ge5p12633sri1k30.png)



# 第二阶段, 抽离DispatcherServlet中耦合的代码, 单一职责
抽离出`DispatcherServlet`中IOC和DI的过程

抽离出以下几个类, 分别是: 
- ApplicationContext: Spring主入口, IOC容器的封装类
    - getBean()方法, 作用是从IOC容器中获取一个Bean

- BeanDefinitionReader: 用于读取配置文件的工具类

- BeanDefinition: **Bean定义**, 封装Bean的配置信息, XML/yml/@annotations

- BeanWrapper: 对原生**Bean对象**和代理对象的统一封装    