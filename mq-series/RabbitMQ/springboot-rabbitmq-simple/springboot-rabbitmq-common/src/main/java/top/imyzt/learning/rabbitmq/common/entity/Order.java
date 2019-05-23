package top.imyzt.learning.rabbitmq.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author imyzt
 * @date 2019/5/22
 * @description Order
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_order")
@Accessors(chain = true)
public class Order implements Serializable {

    private static final long serialVersionUID = -7071587523299395164L;

    @TableId
    private String id;

    private String name;

    /**
     * 存储消息发送的唯一标识
     */
    private String messageId;
}
