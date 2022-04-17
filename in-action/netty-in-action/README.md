# chapter01

使用netty编写一个echo程序.

## 核心对象

1. ChannelHandler - 通道处理器, 负责消息的处理  
1.1 ChannelInboundHandlerAdapter  
1.2 SimpleChannelInboundHandler<T>
2. NioEventLoopGroup - 负责接收和处理新的连接
3. ServerBootstrap - 服务端引导
4. Bootstrap - 客户端引导
5. ChannelFuture 
6. ChannelInitializer

## 服务端

1. 绑定到服务器将在其监听并接受传入连接请求的端口.
2. 配置channel, 以将有关的入站消息通知给EchoServerHandler实例.

## 客户端

1. 连接到服务器.
2. 发送一个或多个消息. 
3. 对于每个消息, 等待并接收从服务器发回的相同的消息. 
4. 关闭连接.

技术端: 
1. 为初始化客户端, 创建一个Bootstrap实例. 
2. 为进行事件处理分配了一个NioEventLoopGroup实例, 其中事件处理包括创建新的连接以及处理入站和出站数据.
3. 为服务器连接创建了一个InetSocketAddress实例.
4. 当连接被建立时, 一个EchoClientHandler实例会被安装到该Channel的ChannelPipeline中.
5. 在一切都设置完成后, 调用Bootstrap.connect()方法连接到远程节点.