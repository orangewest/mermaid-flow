package io.github.orangewest.flow.node;

import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import io.github.orangewest.flow.core.FlowContext;
import io.github.orangewest.flow.meta.FlowNodeMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 条件判断节点
 *
 * @author orangewest
 */
public class CondFlowNode extends AbstractFlowNode {

    /**
     * 需要执行的条件判断表达式
     */
    private Expression expression;

    private final static String RESULT = "_result";

    public CondFlowNode(FlowNodeMeta flowNodeMeta) {
        super(flowNodeMeta);
    }

    @Override
    protected void init() {
        String content = flowNodeMeta.getContent();
        if (StrUtil.isNotBlank(content)) {
            this.expression = AviatorEvaluator.compile(content, true);
        }
    }

    @Override
    protected Object execute0(FlowContext flowContext) {
        if (expression != null) {
            return expression.execute(flowContext.getAllCalcVariables());
        }
        return "true";
    }

    @Override
    protected void executeNextNode(Object executeResult, FlowContext flowContext) {
        if (expression != null) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(RESULT, executeResult);
            filterAndExecuteNextNode(flowContext, resultMap);
        } else {
            Map<String, Object> allVariables = flowContext.getAllCalcVariables();
            filterAndExecuteNextNode(flowContext, allVariables);
        }
    }

    private void filterAndExecuteNextNode(FlowContext flowContext, Map<String, Object> allVariables) {
        List<NextNode> nextNodeList = new ArrayList<>();
        for (NextNode nextNode : this.nextNodes) {
            Boolean execute = (Boolean) nextNode.getExpression().execute(allVariables);
            if (execute) {
                nextNodeList.add(nextNode);
            }
        }
        for (NextNode nextNode : nextNodeList) {
            nextNode.execute(flowContext);
        }
    }

    @Override
    protected Expression buildNextNodeExpression(String condition) {
        if (this.expression != null) {
            if (StrUtil.isBlank(condition)) {
                condition = "true";
            }
            condition = condition + "==" + RESULT;
        }
        return super.buildNextNodeExpression(condition);
    }

}
