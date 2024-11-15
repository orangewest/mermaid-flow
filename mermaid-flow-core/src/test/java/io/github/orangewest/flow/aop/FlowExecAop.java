package io.github.orangewest.flow.aop;

import io.github.orangewest.flow.core.Flow;
import io.github.orangewest.flow.core.FlowContext;
import io.github.orangewest.flow.variable.TypeReference;
import io.github.orangewest.flow.variable.VariableKey;

import java.util.ArrayList;
import java.util.List;

public class FlowExecAop implements FlowAspect {

    public final static VariableKey<Long> TIME = VariableKey.of("time", Long.class);

    public final static VariableKey<List<String>> EXE_CONTEXT = VariableKey.of("exeContext", new TypeReference<List<String>>() {
    });

    @Override
    public void beforeStart(Flow flow, FlowContext flowContext) {
        flowContext.putLocal(TIME, System.currentTimeMillis());
        flowContext.putLocal(EXE_CONTEXT, new ArrayList<>());
    }

    @Override
    public void afterStart(Flow flow, FlowContext flowContext) {
        long time = System.currentTimeMillis() - flowContext.getFromLocal(TIME);
        System.out.println("flowId:" + flow.getFlowId() + " execute time:" + time);
        List<String> fromLocal = flowContext.getFromLocal(EXE_CONTEXT);
        String collect = String.join("==>", fromLocal);
        System.out.println("flowId:" + flow.getFlowId() + " execute count:" + flowContext.getCount() + " context:" + collect);
    }

}
