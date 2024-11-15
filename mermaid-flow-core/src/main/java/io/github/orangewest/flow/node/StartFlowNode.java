package io.github.orangewest.flow.node;

import io.github.orangewest.flow.core.FlowContext;
import io.github.orangewest.flow.meta.FlowNodeMeta;

/**
 * 开始节点
 *
 * @author orangewest
 */
public class StartFlowNode extends AbstractFlowNode {
    public StartFlowNode(FlowNodeMeta flowNodeMeta) {
        super(flowNodeMeta);
    }

    @Override
    protected Object execute0(FlowContext flowContext) {
        return "开始执行";
    }
}
