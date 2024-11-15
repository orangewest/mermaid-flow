package io.github.orangewest.flow.node;

import io.github.orangewest.flow.core.FlowContext;
import io.github.orangewest.flow.function.OptFunction;
import io.github.orangewest.flow.function.OptFunctionFactory;
import io.github.orangewest.flow.meta.FlowNodeMeta;

import java.text.MessageFormat;

/**
 * 操作节点
 *
 * @author orangewest
 */
public class OptFlowNode extends AbstractFlowNode {

    private String[] params;

    private OptFunction optFunction;

    public OptFlowNode(FlowNodeMeta flowNodeMeta) {
        super(flowNodeMeta);
    }

    @Override
    protected void init() {
        String content = flowNodeMeta.getContent();
        int index = content.indexOf("(");
        if (index < 0) {
            index = content.indexOf("=");
            if (index < 0) {
                throw new IllegalArgumentException("解析函数格式错误：" + content);
            }
            content = MessageFormat.format("exp({0})", content.replace("=", ","));
        }
        init(content);
    }

    private void init(String content) {
        int index = content.indexOf("(");
        String functionName = content.substring(0, index);
        OptFunction function = OptFunctionFactory.getFunction(functionName);
        if (function == null) {
            throw new IllegalArgumentException("函数未注册：" + functionName);
        }
        this.optFunction = function;
        String paramStr = content.substring(index + 1, content.length() - 1);
        this.params = paramStr.split(",");
    }

    @Override
    protected Object execute0(FlowContext flowContext) {
        return this.optFunction.execute(this.params, flowContext);
    }

    @Override
    protected void executeNextNode(Object executeResult, FlowContext flowContext) {
        if (executeResult instanceof Boolean) {
            boolean execute = (boolean) executeResult;
            if (execute) {
                super.executeNextNode(true, flowContext);
            }
        }
    }
}
