package top.imyzt.flowable;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.SequenceFlow;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class FlowableSpringBootDemoApplication {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private ProcessEngine processEngine;
    @Resource
    private RepositoryService repositoryService;

    public static void main(String[] args) {
        SpringApplication.run(FlowableSpringBootDemoApplication.class, args);
    }

    @PostConstruct
    /**
     * 初始化流程：加载流程定义，保存到数据库，部署流程。
     * 然后模拟监听“加购”事件，触发对应流程实例启动。
     */
    public void init() throws JsonProcessingException {
        // 1. 读取流程定义JSON
        cn.hutool.json.JSON json = JSONUtil.readJSON(new File("src/main/resources/flowable.json"), StandardCharsets.UTF_8);

        // 2. 解析流程节点
        ObjectMapper objectMapper = new ObjectMapper();
        List<Node<?>> process = objectMapper.readValue(json.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Node.class));
        FlowContext flowContext = parserFlowElement(process);

        // 3. 生成流程唯一编码
        String flowCode = "flow-" + IdUtil.fastSimpleUUID();
        flowContext.setFlowCode(flowCode);
        flowContext.setFlowName("加购优惠券流程");
        flowContext.setFlowDesc("用户加购后等待10分钟，未下单则发放优惠券");
        BpmnModel bpmnModel = flowContext.toBpmnModel();

        // 4. 部署流程到Flowable
        Deployment deploy = repositoryService.createDeployment()
                .key(flowContext.getFlowCode())
                .name(flowContext.getFlowName())
                .addBpmnModel(flowContext.getFlowCode() + ".bpmn20.xml", bpmnModel)
                .deploy();
        System.out.println("流程部署ID: " + deploy.getId());

        // 5. 保存流程模型到数据库
        Model modelData = repositoryService.createModelQuery()
                .modelKey(flowContext.getFlowCode())
                .latestVersion()
                .singleResult();
        if (modelData == null) {
            modelData = repositoryService.newModel();
        }
        modelData.setKey(flowCode);
        modelData.setName(flowContext.getFlowName());
        modelData.setCategory("flow");
        modelData.setDeploymentId(deploy.getId());
        modelData.setMetaInfo(json.toString());
        repositoryService.saveModel(modelData);

        // 6. 模拟监听到“加购”事件，查找所有监听“加购”事件的流程定义，启动流程实例
        // 假设事件码为"add_to_cart"，可根据实际json结构调整
        String eventCode = "AddToCart";
        List<Model> models = repositoryService.createModelQuery()
                .modelKey(flowCode)
                .modelCategory("flow")
                .latestVersion()
                .list();

        for (Model model : models) {
            // 解析模型元信息，判断是否监听了“加购”事件
            // 更加严谨地从StartNode的Props中解析eventCode
            String metaInfo = model.getMetaInfo();
            String modelEventCode = null;
            if (metaInfo != null) {
                try {
                    ObjectMapper om = new ObjectMapper();
                    // 反序列化为List<Node>
                    List<Node<?>> nodeList = om.readValue(metaInfo, om.getTypeFactory().constructCollectionType(List.class, Node.class));
                    for (Node<?> node : nodeList) {
                        // 只处理StartNode类型
                        if (node instanceof top.imyzt.flowable.node.StartNode) {
                            Object props = ((top.imyzt.flowable.node.StartNode) node).getProps();
                            if (props instanceof top.imyzt.flowable.props.StartProps) {
                                modelEventCode = ((top.imyzt.flowable.props.StartProps) props).getEventCode();
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    // 解析失败，忽略
                }
            }
            if (metaInfo != null && metaInfo.contains(eventCode)) {
                // 查询流程定义
                String processDefinitionKey = model.getKey();
                // 启动流程实例
                ProcessInstanceBuilder builder = runtimeService.createProcessInstanceBuilder();
                builder.processDefinitionKey(processDefinitionKey);

                // 设置流程变量
                Map<String, Object> variables = new HashMap<>();
                variables.put("userId", "user_" + IdUtil.fastSimpleUUID());  // 模拟用户ID
                variables.put("productId", "product_" + IdUtil.fastSimpleUUID());  // 模拟商品ID
                variables.put("initiator", "system");
                builder.variables(variables);

                ProcessInstance processInstance = builder.start();
                System.out.println("监听到加购事件，启动流程实例ID: " + processInstance.getId());

                // 查询定时任务
                JobQuery jobQuery = processEngine.getManagementService().createJobQuery().timers();
                for (Job job : jobQuery.list()) {
                    System.out.println("定时任务: " + job.getId() + " - " + job.getDuedate());
                    if (job.getExceptionMessage() != null) {
                        System.out.println("异常信息: " + job.getExceptionMessage());
                    }
                }
            }
        }
    }

    /**
     * 解析流程节点，构建流程上下文
     * @param process 流程节点列表
     * @return FlowContext
     */
    public static FlowContext parserFlowElement(List<Node<?>> process) {
        FlowContext context = new FlowContext();
        List<SequenceFlow> sequences = context.getSequences();

        if (process == null || process.isEmpty()) {
            return context;
        }

        Node<?> prevNode = null;
        for (Node<?> node : process) {
            String currId = node.convert(context);
            if (prevNode != null) {
                SequenceFlow sequence = Node.createSequenceFlow(prevNode.getId(), currId);
                sequences.add(sequence);
            }
            prevNode = node;
        }

        return context;
    }




}
