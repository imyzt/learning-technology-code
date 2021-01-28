# 一发多收(广播)


在微服务场景下,通过fanout模式多个队列绑定exchange模式,不能很好的使用.
因为每个微服务的配置文件完全一样,导致队列名称完全一致,一条消息只会轮询收到一次.

使用exchange绑定一个随机queueName的方式,指定routingKey,实现多个消费者同时收到消息.

发送消息时指定exchange+routingKey,代码在`top.imyzt.learning.mq.rabbitmq.send.Sender`

接收时使用注解, 代码在`top.imyzt.learning.mq.rabbitmq.receiver.Receiver`




参考
[RabbitMq 实现一条消息被多个客户端消费](https://blog.csdn.net/cainiao_xiaowu/article/details/102604684)

问题解决
手动提交下, 遇到的问题, 如果自动ack不会遇到,也无需配置`top.imyzt.learning.mq.rabbitmq.config.MqConfig`
[springBoot-rabbit MQ-设置手动确认ACK-Channel shutdown异常](https://blog.csdn.net/m912595719/article/details/83787486)




