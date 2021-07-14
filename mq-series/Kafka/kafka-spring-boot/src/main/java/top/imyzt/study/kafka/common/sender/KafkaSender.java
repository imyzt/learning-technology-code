package top.imyzt.study.kafka.common.sender;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.imyzt.study.kafka.entity.Message;

/**
 * @author imyzt
 * @date 2019/08/08
 * @description 消息发送对象
 */
@Component
@Slf4j
public class KafkaSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final Gson GSON = new Gson();

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    void send(Message message) {
        String jsonMessage = GSON.toJson(message);
        kafkaTemplate.sendDefault(jsonMessage);
        log.info("send message = {}", jsonMessage);
    }
}
