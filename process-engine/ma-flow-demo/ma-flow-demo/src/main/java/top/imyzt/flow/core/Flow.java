package top.imyzt.flow.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author imyzt
 * @date 2025/05/16
 * @description 流程数据
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Flow {

    /**
     * 流程方向
     */
    private final FlowTurn flowTurn;

    /**
     * 流程数据
     */
    private String data;

    /**
     * 流程提示
     */
    private String tips;

    public static Flow flowData(FlowTurn flowTurn, String data) {
        Flow flow = new Flow(flowTurn);
        flow.setData(data);
        return flow;
    }

    public static Flow flowTips(FlowTurn flowTurn, String tips) {
        Flow flow = new Flow(flowTurn);
        flow.setTips(tips);
        return flow;
    }

    public static Flow retry() {
        return new Flow(FlowTurn.RETRY);
    }

    public static Flow waitVerify(String data) {
        return new Flow(FlowTurn.WAIT_VERIFY, data, null);
    }

    public static Flow next() {
        return new Flow(FlowTurn.NEXT);
    }

    public static Flow end() {
        return new Flow(FlowTurn.END);
    }

    public static Flow error(String error) {
        return flowTips(FlowTurn.ERROR, error);
    }
}
