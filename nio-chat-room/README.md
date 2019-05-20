# 课程内容笔记

## BIO网络模型缺点

- 阻塞式I/O模型
- 弹性伸缩能力差
- 多线程耗资源

## NIO核心

- Channel: 通道
- Buffer: 缓冲区
- Selector: 选择器/多路复用器

![NIO网络模型](https://i.loli.net/2019/05/20/5ce2762401e6569398.png)

## Channel简介: 

- 双向性 
- 非阻塞性
- 操作唯一性,基于数据块的操作.只能通过channel操作

## Channel实现: 

- 文件类: FileChannel
- UDP类:Datagramchannel
- TCP类: ServerSocketChannel/SocketChannel


## Buffer属性

- Capacity: 容量
- Position: 位置, 初始化为0, 当一个写入后回乡后移.max=capaciry-1
- Limit: 上限
- Mark: 标记


## Selector简介:

- 作用: I/O就绪选择
- 地位: NIO网络编程的基础



## NIO编程实现步骤

1. 创建Selector
2. 创建ServerSocketChannel, 并绑定监听端口
3. 将Channel设置为非阻塞模式
4. 将Channel注册到Selector上, 监听连接事件
5. 循环调用Selector的select方法, 检测就绪情况
6. 调用selectedKeys方法获取就绪channel集合
7. 判断就绪事件种类, 调用业务处理方法
8. 根据业务需要决定是否再次注册监听事件, 重复执行第三步操作. 

## NIO 网络编程缺陷

- 麻烦: NIO类库和API繁杂
- 心累: 可靠性能力补齐, 工作量和难度都非常大
- 有坑: Selector空轮询, 导致CPU100%, 出现在类Unix系统上