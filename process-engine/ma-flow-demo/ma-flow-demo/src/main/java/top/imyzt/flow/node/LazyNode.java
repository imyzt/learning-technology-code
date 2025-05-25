package top.imyzt.flow.node;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 分为普通延迟和条件延迟
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LazyNode extends MaNode {

    /**
     * 延迟时间类型
     * 1-延迟固定时间
     * 2-延迟到指定时间
     */
    private Integer lazyType;

    /**
     * 延迟时间
     */
    private Integer day;

    /**
     * 延迟时间
     */
    private Integer hour;

    /**
     * 延迟时间
     */
    private Integer minute;

    /**
     * 延迟时间
     */
    private LocalTime dateTime;

    /**
     * 延迟条件
     */
    private String condition;
}
