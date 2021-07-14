package top.imyzt.study.kafka.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/08/08
 * @description 消息实体
 */
@Data
public class Message<T> {

    private Integer id;

    private T content;

    private LocalDateTime sendTime;
}
