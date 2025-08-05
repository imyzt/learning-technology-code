package top.imyzt.learning.demo.rocketmq5;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.apache.rocketmq.client.java.impl.consumer.PushConsumerBuilderImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author imyzt
 * @date 2025/04/24
 * @description 注册消费监听
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RocketMQConsumerRegister {

  @Resource private ClientConfiguration rocketmqClientConfiguration;
  private PushConsumer pushConsumer;

  @EventListener(value = ApplicationReadyEvent.class)
  public void init() throws ClientException {

    Map<String, FilterExpression> subscriptionExpressions = Map.of(
            "Demo", new FilterExpression("*", FilterExpressionType.TAG));
    this.pushConsumer = new PushConsumerBuilderImpl()
            .setClientConfiguration(rocketmqClientConfiguration)
            // 设置消费者分组。
            .setConsumerGroup("local")
            // 设置预绑定的订阅关系。
            .setSubscriptionExpressions(subscriptionExpressions)
            .setMessageListener(messageView -> {
              log.info("消费到消息：{}", messageView);
              return ConsumeResult.SUCCESS;
            })
            .setMaxCacheMessageCount(20)
            .setMaxCacheMessageSizeInBytes(1024)
            .setConsumptionThreadCount(20).build();

  }

  // @PreDestroy
  // public void destroy() {
  //   this.close();
  // }

  @PostConstruct
  private void addShutdownHook(){
    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }

  @lombok.SneakyThrows
  private void close() {
    if (pushConsumer != null) {
      log.info("消费监听组件 [{}] 开始关闭", pushConsumer);
      pushConsumer.close();
      log.info("消费监听组件 [{}] 关闭成功", pushConsumer);
    }
  }

}
