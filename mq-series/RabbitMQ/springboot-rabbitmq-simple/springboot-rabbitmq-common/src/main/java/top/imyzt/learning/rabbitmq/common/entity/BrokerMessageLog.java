package top.imyzt.learning.rabbitmq.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/5/23
 * @description 消息队列保存对象
 */
@Data
@Accessors(chain = true)
@TableName("broker_message_log")
@AllArgsConstructor
@NoArgsConstructor
public class BrokerMessageLog implements Serializable {

    private static final long serialVersionUID = -9088632133309563231L;

    @TableId
    private String messageId;

    private String message;

    private Integer tryCount;

    private String status;

    private LocalDateTime nextRetry;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
