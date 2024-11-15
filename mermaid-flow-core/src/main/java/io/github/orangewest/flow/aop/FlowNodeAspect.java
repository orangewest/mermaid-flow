package io.github.orangewest.flow.aop;

import io.github.orangewest.flow.core.FlowContext;
import io.github.orangewest.flow.node.AbstractFlowNode;

/**
 * 节点切面
 *
 * @author orangewest
 */
public interface FlowNodeAspect {

    /**
     * 节点执行前
     *
     * @param flowNode    当前节点
     * @param flowContext 上下文
     */
    default void beforeExecute(AbstractFlowNode flowNode, FlowContext flowContext) {

    }

    /**
     * 节点执行后
     *
     * @param flowNode    当前节点
     * @param flowContext 上下文
     */
    default void afterExecute(AbstractFlowNode flowNode, FlowContext flowContext) {

    }

}
