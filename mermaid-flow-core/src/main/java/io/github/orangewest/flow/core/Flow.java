package io.github.orangewest.flow.core;

import io.github.orangewest.flow.aop.FlowAspect;
import io.github.orangewest.flow.aop.FlowAspectManager;
import io.github.orangewest.flow.enums.FlowNodeType;
import io.github.orangewest.flow.exception.FlowNodeNotExistsException;
import io.github.orangewest.flow.meta.FlowLinkMeta;
import io.github.orangewest.flow.meta.FlowMeta;
import io.github.orangewest.flow.meta.FlowNodeMeta;
import io.github.orangewest.flow.node.AbstractFlowNode;
import io.github.orangewest.flow.node.CondFlowNode;
import io.github.orangewest.flow.node.OptFlowNode;
import io.github.orangewest.flow.node.StartFlowNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程定义
 *
 * @author orangewest
 */
public class Flow {

    /**
     * 流程ID
     */
    private final String flowId;

    /**
     * 流程上下文
     */
    private FlowContext flowContext;

    /**
     * 节点集合
     */
    private final Map<String, AbstractFlowNode> flowNodeMap = new ConcurrentHashMap<>();

    /**
     * 起始节点
     */
    private FlowNode startNode;

    /**
     * 是否初始化
     */
    private volatile boolean init;

    public Flow(String flowId, FlowMeta flowMeta) {
        this.flowId = flowId;
        initNode(flowMeta);
    }

    private void initNode(FlowMeta flowMeta) {
        List<FlowNodeMeta> nodeMetaList = flowMeta.getNodeMetaList();
        for (FlowNodeMeta flowNodeMeta : nodeMetaList) {
            addNode(flowNodeMeta.getNodeId(), createNode(flowNodeMeta));
        }
        List<FlowLinkMeta> linkMetaList = flowMeta.getLinkMetaList();
        for (FlowLinkMeta flowLinkMeta : linkMetaList) {
            AbstractFlowNode fromNode = getNode(flowLinkMeta.getFromNodeId());
            AbstractFlowNode toNode = getNode(flowLinkMeta.getToNodeId());
            String condition = flowLinkMeta.getCondition();
            fromNode.addNextNode(condition, toNode);
        }
    }

    private AbstractFlowNode createNode(FlowNodeMeta flowNodeMeta) {
        FlowNodeType nodeType = flowNodeMeta.getNodeType();
        AbstractFlowNode flowNode;
        switch (nodeType) {
            case START:
                flowNode = new StartFlowNode(flowNodeMeta);
                break;
            case OPT:
                flowNode = new OptFlowNode(flowNodeMeta);
                break;
            case COND:
                flowNode = new CondFlowNode(flowNodeMeta);
                break;
            default:
                throw new IllegalArgumentException("不支持的节点类型：" + nodeType);
        }
        return flowNode;
    }

    public String getFlowId() {
        return flowId;
    }

    public FlowContext getFlowContext() {
        return flowContext;
    }


    public void setFlowContext(FlowContext flowContext) {
        this.flowContext = flowContext;
    }

    public void init() {
        if (startNode == null) {
            throw new IllegalArgumentException("flowId:" + flowId + " has no start node");
        }
        this.init = true;
    }


    public void addNode(String nodeId, AbstractFlowNode flowNode) {
        if (flowNode instanceof StartFlowNode) {
            if (startNode != null) {
                throw new IllegalArgumentException("flowId:" + flowId + " has more than one start node");
            }
            this.startNode = flowNode;
        }
        flowNodeMap.put(nodeId, flowNode);
    }

    public void updateNodeContent(String nodeId, String content) {
        AbstractFlowNode flowNode = getNode(nodeId);
        flowNode.updateContent(content);
    }

    public void updateNodeLink(String nodeId, String nextNodeId, String expression) {
        AbstractFlowNode flowNode = getNode(nodeId);
        flowNode.updateLink(nextNodeId, expression);
    }


    public AbstractFlowNode getNode(String nodeId) {
        return Optional.ofNullable(flowNodeMap.get(nodeId))
                .orElseThrow(() -> new FlowNodeNotExistsException(this.flowId, nodeId));
    }

    /**
     * 执行流程
     */
    public void start() {
        if (!init) {
            throw new IllegalArgumentException("flowId:" + flowId + " has not been initialized");
        }
        try {
            this.flowContext.addCount();
            List<FlowAspect> flowAspects = FlowAspectManager.getFlowAspects();
            for (FlowAspect flowAspect : flowAspects) {
                flowAspect.beforeStart(this, this.flowContext);
            }
            this.startNode.execute(this.flowContext);
            for (FlowAspect flowAspect : flowAspects) {
                flowAspect.afterStart(this, this.flowContext);
            }
        } finally {
            this.flowContext.reset();
        }
    }

    /**
     * 销毁流程
     */
    public void destroy() {
        this.init = false;
        this.flowContext.destroy();
    }


}
