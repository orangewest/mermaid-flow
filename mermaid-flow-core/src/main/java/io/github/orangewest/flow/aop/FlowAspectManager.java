package io.github.orangewest.flow.aop;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程切面管理器
 *
 * @author orangewest
 */
public class FlowAspectManager {

    /**
     * 流程切面列表
     */
    private final static List<FlowAspect> FLOW_ASPECTS = new ArrayList<>();

    /**
     * 节点切面列表
     */
    private final static List<FlowNodeAspect> FLOW_NODE_ASPECTS = new ArrayList<>();

    /**
     * 添加切面
     *
     * @param flowAspect 切面
     */
    public static void addFlowAspect(FlowAspect flowAspect) {
        FLOW_ASPECTS.add(flowAspect);
    }

    /**
     * 添加节点切面
     *
     * @param flowNodeAspect 节点切面
     */
    public static void addFlowNodeAspect(FlowNodeAspect flowNodeAspect) {
        FLOW_NODE_ASPECTS.add(flowNodeAspect);
    }

    /**
     * 获取切面列表
     *
     * @return 切面列表
     */
    public static List<FlowAspect> getFlowAspects() {
        return FLOW_ASPECTS;
    }

    /**
     * 获取节点切面列表
     *
     * @return 节点切面列表
     */
    public static List<FlowNodeAspect> getFlowNodeAspects() {
        return FLOW_NODE_ASPECTS;
    }

}
