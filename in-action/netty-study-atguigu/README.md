# NIO 与 BIO的比较

1. BIO以流的形式处理数据, 而NIO以块的形式处理数据, 块IO的效率比NIO快很多
2. BIO是阻塞的, NIO是非阻塞的
3. BIO基于字节流和字符流进行操作, 而NIO基于Channel和Buffer进行操作, 数据
总是从通道读取到缓冲区或从缓冲区写入到通道, Selector用于监听多个通道的事件(比如连接请求, 数据到达),
因此可以使用单个线程就可以监听多个客户端通道.


# NIO三大核心原理示意图
 ![图片](https://github.com/imyzt/learning-technology-code/raw/master/in-action/netty-study-atguigu/docs/images/NIO_core.png)
 
 1. 每个Channel都会对应一个Buffer
 2. Selector对应一个线程, 一个县城对应多个Channel
 3. 该图反映了有三个Channel注册到Selector上
 4. 程序切换到哪个Channel是由事件决定的, Event是一个重要的概念
 5. Selector会根据不同的事件, 在各个通道上切换
 6. Buffer就是一个内存块, 底层是一个数组
 7. 数据的读取和写入是通过Buffer, 和BIO不同, BIO要么是输入流或者是输出流,不能双向,
    但是NIO的Buffer可以是度也可以是写, 需要`flip`方法切换
 8. Channel是双向的, 可以返回的层操作系统的情况, 比如Linux的底层就是双向的