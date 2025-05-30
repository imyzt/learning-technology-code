package top.imyzt.flowable;

import lombok.Getter;
import lombok.Setter;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.springframework.util.CollectionUtils;
import top.imyzt.flowable.node.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

@Getter
public class FlowContext {

    @Setter
    private String flowCode;
    @Setter
    private String flowName;
    @Setter
    private String flowDesc;
    @Setter
    private String applicationCode;

    private final Map<String, FlowElement> elementMap = new TreeMap<>();

    private final List<SequenceFlow> sequences = new ArrayList<>();

    private final List<Node<?>> taskNodeList = new ArrayList<>();

    private final Map<String, List<String>> nodeChildMap = new LinkedHashMap<>();


    public void collectNode(Node<?> node, Node<?>... childNodes) {
        List<Node<?>> nodeList = List.of(childNodes);
        if (!CollectionUtils.isEmpty(nodeList)) {
            List<String> group = nodeChildMap.computeIfAbsent(node.getId(), k -> new ArrayList<>());
            group.addAll(nodeList.stream().map(Node::getId).toList());
        }
    }

    public void collectTaskNode(Node<?> node) {
        if (Objects.nonNull(node)) {
            taskNodeList.add(node);
        }
    }


    public BpmnModel toBpmnModel() {
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.setTargetNamespace("http://flowable.org/bpmn");
        bpmnModel.setExporter("Flowable Open Source Modeler");

        // 创建流程定义
        Process process = new Process();
        process.setId(Optional.ofNullable(this.getFlowCode()).orElse(Node.generateId("flow-")));
        process.setName(Optional.ofNullable(this.getFlowName()).orElse("流程模型-" + process.getId()));
        process.setDocumentation(this.getFlowDesc());

        // 添加所有节点
        for (FlowElement element : elementMap.values()) {
            process.addFlowElement(element);
        }

        // 添加所有连线
        for (SequenceFlow sequence : sequences) {
            if (sequence.getSourceRef() != null && sequence.getTargetRef() != null) {
                process.addFlowElement(sequence);
            }
        }

        // 设置流程
        bpmnModel.addProcess(process);

        // 自动布局
        try {
            BpmnAutoLayout autoLayout = new BpmnAutoLayout(bpmnModel);
            autoLayout.execute();
        } catch (Exception e) {
            // 如果自动布局失败，至少保证流程可以执行
            System.err.println("自动布局失败: " + e.getMessage());
        }

        return bpmnModel;
    }

}
