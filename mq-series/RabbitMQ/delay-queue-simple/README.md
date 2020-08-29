
# 参考文章
- https://www.cnblogs.com/mfrank/p/11260355.html
- https://blog.csdn.net/u014308482/article/details/53036770
- https://blog.csdn.net/u012988901/article/details/88958654

# 本文大纲

![http://blog.imyzt.top/upload/2020/08/tha3ddbt1si8dr39tpjkmbfem6.jpg](http://blog.imyzt.top/upload/2020/08/tha3ddbt1si8dr39tpjkmbfem6.jpg)


# 什么是延迟队列

**延迟队列也是队列，队列就意味着元素是有序的。元素出队和入队是有方向的，从一端进，从另一端出。**
延迟队列体现在延迟上面，普通的队列是希望元素能够被更快的消费，而延迟队列是希望元素在指定的时间被消费。所以**延迟队列里面的元素是带有时间属性**的。

# 延迟队列的使用场景

- 用户订单10分钟内未支付自动取消
- 预定会议后，开会前10分钟提醒
- 优惠券到期前提醒

# 基础知识补充

## TTL（Time To Live）

### 什么是TTL

TTL是RabbitMQ中一个消息或者队列的属性，表明一条消息或者该队列中所有消息的最大存活时间，单位是毫秒。如果一条设置了TTL属性的消息或者一条消息进入设置了TTL属性的队列后，那么这条消息在设置的时间内没有被消费，则会成为“死信”，如果消息配置了TTL后被投递到设置了TTL属性的队列中，则按照较小的那个值设置。

### 如何设置TTL

如果不设置TTL，则消息永远不会过期。
如果TTL=0，则表示除非此时可以直接投递到该消息的消费者，否则这条消息就会被丢弃。

### 创建队列时，设置队列的TTL
Map<String, Object> args = new HashMap<String, Object>();
args.put("x-message-ttl", 6000);
channel.queueDeclare(queueName, durable, exclusive, autoDelete, args);

### 设置每条消息的TTL
AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
builder.expiration("6000");
AMQP.BasicProperties properties = builder.build();
channel.basicPublish(exchangeName, routingKey, mandatory, properties, "msg body".getBytes());

### 两种TTL的特性

两种设置方式有很大的区别
- 如果设置了队列的TTL，那么一旦消息过期，就会被队列丢弃。
- 针对每条消息设置TTL，即使消息过期，也不会马上丢弃，因为消息是否过期是在即将投递到消费者之前确定的，如果当前队列有严重的消息积压，则已经过期的消息也许还能存活很长时间。

上述第2点，RabbitMQ只会检查第一个消息是否过期，消息过期还存活的原因是因为队列是有序消费的，而如果需要判断每条消息是否过期则需要遍历整个队列，性能损耗太大，选择在有序消费到该消息时准备投递前进行消息的判断，空间换时间的方案。


## DLX（Dead Letter Exchanges）

### 什么是DLX

DLX的作用就是用来接收死信消息，当一个消息在队列中变成了死信消息后，可以发送到另一个exchange（交换机），这个交换机就是DLX，绑定DLX的队列成为死信队列，当这个队列存在死信消息时，RabbitMQ就会立即将这个消息发布到设置的DLX上去，进而被路由到绑定该DLX的死信队列上。

### 什么是死信

消息被拒绝 （Basic.Reject/Basic.Nack），并且设置requeue=false
消息过期（TTL）
队列达到最大长度

### 如何设置DLX

RabbitMQ的Queue可以配置x-dead-letter-exchange 和x-dead-letter-routing-key（可选）两个参数，如果队列内出现了dead letter，则按照这两个参数重新路由转发到指定的队列。

x-dead-letter-exchange：出现dead letter之后将dead letter重新发送到指定exchange
x-dead-letter-routing-key：出现dead letter之后将dead letter重新按照指定的routing-key发送

## 基础知识总结

结合TTL和DLX两个特性，将消息设置了TTL规则之后当消息在队列中变为dead letter时，利用DLX特性将它转发到另一个Exchange或者Routing Key，这个时候绑定这个死信队列的消费者开始消费消息即可实现延时消费的效果。

生产者生产一条延时消息，根据需要延时时间的不同，利用不同的routingKey将消息路由到不同的延时队列①，每个队列都设置了不同的TTL属性，并绑定在同一个死信交换机中，消息过期后，根据routingKey的不同，又会被路由到不同的死信队列中，消费者只需要监听对应的死信队列进行处理即可。

①：不同消息绑定在不同的队列中很重要，此处使用的是TTL的第一种，为队列设置时长。可以确保队列中消息的过期时间是有序的。因为如果队列中有不同过期时间的消息，会出现消息错乱的情况。比如第一条是10分钟过期，第二条是20秒过期，则必须要等第一条消息有序被消费后（结合TTL过期特性，空间换时间），才能在10分钟20秒后消费到第二条消息。


# 实现延迟消息队列

## 源码


### 配置部分

```
/**
 * 代码: https://www.cnblogs.com/mfrank/p/11260355.html
 * @author imyzt
 * @date 2020/08/29
 * @description 配置文件
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 延迟交换机
     */
    public static final String DELAY_EXCHANGE_NAME = "delay.queue.demo.business.exchange";
    /**
     * 延迟队列名称
     */
    private static final String DELAY_QUEUEA_NAME = "delay.queue.demo.business.queuea";
    private static final String DELAY_QUEUEB_NAME = "delay.queue.demo.business.queueb";
    /**
     * 延迟队列Routing Key
     */
    public static final String DELAY_QUEUEA_ROUTING_KEY = "delay.queue.demo.business.queuea.routingkey";
    public static final String DELAY_QUEUEB_ROUTING_KEY = "delay.queue.demo.business.queueb.routingkey";
    /**
     * 死信交换机
     */
    private static final String DEAD_LETTER_EXCHANGE = "delay.queue.demo.deadletter.exchange";
    /**
     * 死信队列Routing Key
     */
    private static final String DEAD_LETTER_QUEUEA_ROUTING_KEY = "delay.queue.demo.deadletter.delay_10s.routingkey";
    private static final String DEAD_LETTER_QUEUEB_ROUTING_KEY = "delay.queue.demo.deadletter.delay_60s.routingkey";
    /**
     * 死信队列名称
     */
    public static final String DEAD_LETTER_QUEUEA_NAME = "delay.queue.demo.deadletter.queuea";
    public static final String DEAD_LETTER_QUEUEB_NAME = "delay.queue.demo.deadletter.queueb";

    /**
     * 首先声明延迟队列, 生产者通过交换机和Routing Key将消息发送到延迟队列上
     * 然后消息变为死信时, 死信交换机将消息转发到死信队列上, 消费者对死信队列进行监听
     */

    /**
     * 声明延迟队列交换机
     */
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }
    /**
     * 声明死信队列交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     * 声明延迟队列A
     * 延迟10秒
     * 并绑定到对应的死信交换机
     */
    @Bean
    public Queue delayQueueA() {
        Map<String, Object> args = new HashMap<>(3);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEA_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(DELAY_QUEUEA_NAME).withArguments(args).build();
    }
    /**
     * 声明延迟队列B
     * 延迟60秒
     * 并绑定到对应的死信交换机
     */
    @Bean
    public Queue delayQueueB() {
        Map<String, Object> args = new HashMap<>(3);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEB_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 60000);
        return QueueBuilder.durable(DELAY_QUEUEB_NAME).withArguments(args).build();
    }

    /**
     * 声明死信队列A, 用于接收延迟10s的消息
     */
    @Bean
    public Queue deadLetterQueueA() {
        return new Queue(DEAD_LETTER_QUEUEA_NAME);
    }
    /**
     * 声明死信队列B, 用于接收延迟60s的消息
     */
    @Bean
    public Queue deadLetterQueueB() {
        return new Queue(DEAD_LETTER_QUEUEB_NAME);
    }

    /**
     * 声明延迟队列A与延迟队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding delayBindingA(@Qualifier("delayQueueA") Queue queue,
                                                                   @Qualifier("delayExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEA_ROUTING_KEY).noargs();
    }
    /**
     * 声明延迟队列B与延迟队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding delayBindingB(@Qualifier("delayQueueB") Queue queue,
                                                                   @Qualifier("delayExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEB_ROUTING_KEY).noargs();
    }

    /**
     * 声明死信队列A与死信队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding deadBindingA(@Qualifier("deadLetterQueueA") Queue queue,
                                                                   @Qualifier("deadLetterExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEA_ROUTING_KEY).noargs();
    }
    /**
     * 声明死信队列B与死信队列交换机绑定关系
     * Routing Key
     */
    @Bean
    public Binding deadBindingB(@Qualifier("deadLetterQueueB") Queue queue,
                                                                  @Qualifier("deadLetterExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEB_ROUTING_KEY).noargs();
    }

}
```

### 生产者部分

```
/**
 * @author imyzt
 * @date 2020/08/29
 * @description 消息生产者
 */
@Slf4j
@RestController
@RequestMapping("sender")
public class MessageSenderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public void sender(String msg, String type) {

        log.info("当前时间：{},收到请求，msg:{},delayType:{}", LocalDateTime.now().toString(), msg, type);

        switch (type) {
            case "DELAY_10S":
                rabbitTemplate.convertAndSend(DELAY_EXCHANGE_NAME, DELAY_QUEUEA_ROUTING_KEY, msg);
                break;
            case "DELAY_60S":
                rabbitTemplate.convertAndSend(DELAY_EXCHANGE_NAME, DELAY_QUEUEB_ROUTING_KEY, msg);
                break;
            default:
                break;
        }
    }
}
```

### 消费者部分

```
/**
 * @author imyzt
 * @date 2020/08/29
 * @description 死信队列消费者
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = DEAD_LETTER_QUEUEA_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},死信队列A收到消息：{}", LocalDateTime.now().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = DEAD_LETTER_QUEUEB_NAME)
    public void receiveB(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},死信队列B收到消息：{}", LocalDateTime.now().toString(), msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
```


## 两个交换机
![http://blog.imyzt.top/upload/2020/08/pu6dgrcf7kjtuqqahres3bp48v.jpg](http://blog.imyzt.top/upload/2020/08/pu6dgrcf7kjtuqqahres3bp48v.jpg)

## 四个队列
![http://blog.imyzt.top/upload/2020/08/3vtkq5kvviivlrlfvtd7mdtja3.jpg](http://blog.imyzt.top/upload/2020/08/3vtkq5kvviivlrlfvtd7mdtja3.jpg)

## 延迟效果
![http://blog.imyzt.top/upload/2020/08/0hr7c5k8ochhhrfq0t2lldo29b.jpg](http://blog.imyzt.top/upload/2020/08/0hr7c5k8ochhhrfq0t2lldo29b.jpg)

## 缺陷

从上面的效果来看，第一条消息在10秒后变成了死信消息，然后被消费掉。第二条消息在60米哦按后变成了死信队列，然后被消费掉。目前来看基本功能的延迟队列就算完成了。  
但是有一个问题就是，队列的消息都是有序的失效，如果增加一个新的时间需求，那么有需要增加一个队列处理上面的逻辑，实在是不够优雅。
