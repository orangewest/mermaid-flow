package io.github.orangewest.flow.aop;

import io.github.orangewest.flow.core.FlowContext;
import io.github.orangewest.flow.node.AbstractFlowNode;

import java.util.List;

public class FlowNodeExecAop implements FlowNodeAspect {

    @Override
    public void beforeExecute(AbstractFlowNode flowNode, FlowContext flowContext) {
//        System.out.println("beforeExecute:" + flowNode.getFlowNodeMeta().getNodeId());
    }

    @Override
    public void afterExecute(AbstractFlowNode flowNode, FlowContext flowContext) {
        List<String> context = flowContext.getFromLocal(FlowExecAop.EXE_CONTEXT);
        context.add(flowNode.getFlowNodeMeta().getNodeId());
    }

}
