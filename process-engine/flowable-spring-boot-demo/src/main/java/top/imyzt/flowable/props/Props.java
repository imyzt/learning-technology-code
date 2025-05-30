package top.imyzt.flowable.props;


import lombok.Data;

import java.util.List;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
@Data
public class Props {

    private Listener delegate;

    private List<Listener> executionListeners;

    @Data
    public static class Listener {

        private String event;
        private String implementationType;
        private String implementation;

        private String param;
    }
}
