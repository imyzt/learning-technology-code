package top.imyzt.study.kafka.common.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author imyzt
 * @date 2019/08/08
 * @description 描述信息
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(id = "test-id", topics = "kafka.test-id")
    public void listener(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<? extends ConsumerRecord<?, ?>> optional = Optional.ofNullable(record);

        if (optional.isPresent()) {
            ConsumerRecord<?, ?> consumerRecord = optional.get();

            log.info("topic = {}, record = {}", topic, record);
            log.info("message = {}", consumerRecord);
        }
    }
}
