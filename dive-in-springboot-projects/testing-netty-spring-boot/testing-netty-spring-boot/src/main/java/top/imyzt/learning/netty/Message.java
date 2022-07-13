package top.imyzt.learning.netty;

import java.io.Serializable;

/**
 * @author imyzt
 * @date 2022/07/02
 * @description 消息对象
 */
public record Message(Long messageId,
                      String content) implements Serializable {

}