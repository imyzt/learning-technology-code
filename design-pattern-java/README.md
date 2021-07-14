# Java设计模式学习

持续更新博客地址 [http://imyzt.top/category/java-design-patterns](http://imyzt.top/category/java-design-patterns)


## 代理模式
top.**.dp.proxy包下的代码  
[http://imyzt.top/article/87](http://imyzt.top/article/87)

JDK动态代理流程:   

client(Main方法) -> 代理类(Proxy.newProxyInstance生成的对象) -> 
适配器(InvocationHandler作为适配器) -> 目标类(最终的目标类)

### URL代理

通常,调用第三方JAR包, 含有HTTP请求时, 无法对HTTP请求进行拦截处理(比如获取请求时长等), 可以通过两种方案实现代理`URL`HTTP处理器  

1. java.net.URL#setURLStreamHandlerFactory方法
2. 通过`GetPropertyAction.privilegedGetProperty(protocolPathProp)`方法获取协议处理器, 
默认是`sun.net.www.protocol`, 每一种协议在此包下均有独立的处理器`handler`, 
通过`java.lang.System.setProperties`修改`java.protocol.handler.pkgs`属性为自定义实现后可以支持任意处理.   