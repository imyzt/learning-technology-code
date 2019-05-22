# rabbitmq-simple

CSDN博客 [RabbitMQ入门教程](https://blog.csdn.net/chwshuang/article/details/50521708) 系列学习笔记  

  
# development environment

- Windows10
- JDK1.8
- Intellij IDEA
- RabbitMQ 3.7.8 for Windows


# RabbitMQ交换器规则

- direct 直连模式
- topic 主题模式
- headers 标题模式
- fanout 分发模式

通过 `channel.exchangeDeclare("logs", "fanout");` 修改交换器模式.  
通过 `rabbitmqctl list_exchanges` 列出服务器上所有可用的交换器.  


## fanout模式

fanout模式下, 如果当前队列没有被绑定到交换器, 消息将被丢弃  
所有的消费者都会收到同样的内容  


## direct模式

消息路由模式, 通过将交换机和RoutingKey绑定, 可以只获取关心的内容.

## topic(匹配)模式

- [*] 表示一个单词
- [#] 表示零个或多个单词  

如果消费端的路由关键字只使用[#]来匹配消息, 在匹配[topic]模式下, 他会变成一个分发[fanout]模式.  
如果消费端的路有关键字没有[*]或者[#],他就会变成直连[direct]模式.  


# topic(匹配)模式, 三个测试题

1. 在匹配交互器模式下，消费者端路由关键字 “*” 能接收到生产者端发来路由关键字为空的消息吗？  

答: 经过 `top.imyzt.learning.rabbitmq.topic.test1` 验证, 无法接收  

2. 在匹配交换器模式下，消费者端路由关键字“#.*”能接收到生产者端以“..”为关键字的消息吗？如果发送来的消息只有一个单词，能匹配上吗？

答: 经过 `top.imyzt.learning.rabbitmq.topic.test2` 验证, 两种均能接收


3. “a.*.#” 与 “a.#” 有什么不同吗？

答: 没有不同