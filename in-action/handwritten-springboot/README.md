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

- BeanWrapper: 对原生**Bean对象**和**代理对象**的**统一封装**    


## 总结流程

// ApplicationContext构造方法
```
ApplicationContext() {
    // 从配置文件读取bean的定义
    List<BeanDefinition> beanDefinitions = reader.doLoadBeanDefinitions();
    
    // 将bean的定义保存到beanDefinitionMap中
    beanDefinitionMap.put beanDefinitions
}
```

// ApplicationContext.getBean()方法
```
ApplicationContext.getBean() {
    // 初始化bean的实例
    instaniateBean() {
        // 判断bean的ClassName是否符合AOP规则
        // 如果符合则创建**代理对象**
        // 否则创建**原始对象**
    }
    
    // 封装成BeanWrapper对象, 统一对象的结构
    
    // 完成依赖注入
    populateBean()
}
```

# 第三阶段, MVC流程

- HandlerMapping  完成URL和Method的对应关系
- HandlerAdapter  完成方法参数的动态匹配
- ViewResolver  完成模板页面的解析和渲染

- ModalAndView  封装传参信息和模板的名字
- View  封装模板文件的解析过程


## 总结流程

// 初始化阶段
```
initStrategies() {
    initHandlerMapping();
    initHandlerAdapter();
    initViewResolver();
}
```

// 运行阶段
```
doDispatch() {
    HandlerMapping handler = getHandler(req);
    HandlerAdapter ha = getHandlerAdapter(handler);
    ModalAndView mv = ha.handler(req, resp, handler);

    processDispatchResult(req, resp, mv) {
        View view = ViewResolver.resolverViewName(mv.getViewName());
        view.reader(mv.getModal());
    }
}
```