package top.imyzt.flow.process;


import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import top.imyzt.flow.node.LazyNode;
import top.imyzt.flow.core.Flow;

import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 延迟节点
 */
@Slf4j
public class LazyNodeProcess extends NodeProcess<LazyNode> {
    @Override
    public Flow process(LazyNode node, String nodeCode) {
        return Flow.waitVerify(DateUtil.now());
    }

    @Override
    public Flow check(LazyNode node, String verifyData) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime toTime = DateUtil.parseLocalDateTime(verifyData);
        if (node.getLazyType() == null || node.getLazyType() == 1) {
            if (node.getDay() != null) {
                toTime = toTime.plusDays(node.getDay());
            }
            if (node.getHour() != null) {
                toTime = toTime.plusHours(node.getHour());
            }
            if (node.getMinute() != null) {
                toTime = toTime.plusMinutes(node.getMinute());
            }
            return now.isAfter(toTime) ? Flow.next() : Flow.waitVerify(null);
        } else {
            if (node.getDay() != null) {
                toTime = toTime.plusDays(node.getDay());
            }
            if (node.getDateTime() != null) {
                toTime = LocalDateTime.of(toTime.toLocalDate(), node.getDateTime());
            }
            return now.isAfter(toTime) ? Flow.next() : Flow.waitVerify(null);
        }

    }
}
