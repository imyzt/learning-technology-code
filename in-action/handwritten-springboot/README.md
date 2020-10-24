# DispatcherServlet核心处理流程

1. 加载配置文件, application.properties
2. 读取scanPackage下所有的javaClass, 存放起来
3. 读取所有javaClass中标注了@Service或@RestController的需要托管的Bean, 通过反射创建instance存放在Bean工厂
4. 读取所有bean中每一个bean的每一个标注了@Autowired的Field, 通过在IOC Bean工厂中查找, 反射设置值完成DI
5. 读取所有bean中标注了@RestController的控制器, 通过读取每一个标注了@RequestMapping的方法, 将其完成URL < - > Method的绑定, 存放在HandlerMapping中
6. doDispatch()方法获取请求URL, 匹配HandlerMapping则通过invoke()反射调用方法处理
