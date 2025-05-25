package top.imyzt.flowable;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.job.api.Job;
import org.flowable.job.api.JobQuery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.imyzt.flowable.node.Node;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class FlowableSpringBootDemoApplication {

    private static final String START_EVENT_ID = "start";
    private static final String END_EVENT_ID = "end";
    // @Resource
    // private ProcessService processService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private ProcessEngine processEngine;
    @Resource
    private RepositoryService repositoryService;
    // @Resource
    // private MyTaskService taskService;

    public static void main(String[] args) {
        SpringApplication.run(FlowableSpringBootDemoApplication.class, args);
    }

    @PostConstruct
    public void init() throws JsonProcessingException {

        cn.hutool.json.JSON json = JSONUtil.readJSON(new File("src/main/resources/flowable.json"), StandardCharsets.UTF_8);


        ObjectMapper objectMapper = new ObjectMapper();
        List<Node<?>> process  = objectMapper.readValue(json.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Node.class));
        FlowContext flowContext = parserFlowElement(process);
        String flowCode = "flow-" + IdUtil.fastSimpleUUID();
        flowContext.setFlowCode(flowCode);
        flowContext.setFlowName("流程模型");
        flowContext.setFlowDesc("flowable remark");
        BpmnModel bpmnModel = flowContext.toBpmnModel();


        Deployment deploy = repositoryService.createDeployment().key(flowContext.getFlowCode()).name(flowContext.getFlowName())
                .addBpmnModel(flowContext.getFlowCode() + ".bpmn20.xml", bpmnModel)
                .deploy();
        System.out.println(deploy.getId());

        // ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
        //         .deploymentId(deploy.getId()).singleResult();

        Model modelData = repositoryService.createModelQuery().modelKey(flowContext.getFlowCode()).latestVersion().singleResult();
        if (modelData == null) {
            modelData = repositoryService.newModel();
        }

        modelData.setKey(flowCode);
        modelData.setName(flowContext.getFlowName());
        modelData.setCategory("flow");
        modelData.setDeploymentId(deploy.getId());
        modelData.setMetaInfo(json.toString());
        repositoryService.saveModel(modelData);

        ProcessInstanceBuilder builder = runtimeService.createProcessInstanceBuilder();
        builder.processDefinitionKey(flowCode);
        Map<String, Object> variables = new HashMap<>();
        variables.put("initiator", "imyzt");
        builder.variables(variables);
        ProcessInstance processInstance = builder.start();
        System.out.println(processInstance.getId());

        JobQuery jobQuery = processEngine.getManagementService().createJobQuery().timers();
        for (Job job : jobQuery.list()) {
            System.out.println("Timer job: " + job.getId() + " - " + job.getDuedate());
            if (job.getExceptionMessage() != null) {
                System.out.println("Exception message: " + job.getExceptionMessage());
            }
        }

        //
        //
        // String processId = processService.startProcess("imyzt");
        // System.out.println(processId);
        //
        // List<Task> imyztTaskList = taskService.getTasks("imyzt");
        // for (Task task : imyztTaskList) {
        //     System.out.println(task.getName());
        //     taskService.completeTask(task.getId());
        // }
    }

    public static FlowContext parserFlowElement(List<Node<?>> process) {

        // 初始化上下文
        FlowContext context = new FlowContext();
        Map<String, FlowElement> elementMap = context.getElementMap();
        List<SequenceFlow> sequences = context.getSequences();

        // 创建开始事件
        StartEvent startEvent = new StartEvent();
        startEvent.setId(START_EVENT_ID);
        elementMap.put(startEvent.getId(), startEvent);

        // 创建结束事件
        EndEvent endEvent = new EndEvent();
        endEvent.setId(END_EVENT_ID);
        elementMap.put(endEvent.getId(), endEvent);

        String startTargetRef = null;
        String endSourceRef = null;

        // 中间元素连线
        List<SequenceFlow> sequencePath = new ArrayList<>();
        for (int i = 0, pathNum = process.size(); i < pathNum; i++) {
            Node<?> node = process.get(i);

            String startId = node.getId();
            // 递归解析元素
            String endId = node.convert(context);

            if (i == 0) {
                startTargetRef = startId;
            }

            if (i == pathNum - 1) {
                endSourceRef = endId;
            }

            if (i < pathNum - 1) {
                Node<?> nextNode = process.get(i + 1);
                String nextId = nextNode.getId();

                SequenceFlow sequence = Node.createSequenceFlow(endId, nextId);
                sequencePath.add(sequence);
            }
        }

        // 起始连线、结束连线
        SequenceFlow startSequence = Node.createSequenceFlow(START_EVENT_ID, startTargetRef);
        SequenceFlow endSequence = Node.createSequenceFlow(endSourceRef, END_EVENT_ID);
        sequences.add(startSequence);
        if (!sequencePath.isEmpty()) {
            sequences.addAll(sequencePath);
        }
        sequences.add(endSequence);

        return context;
    }




}
