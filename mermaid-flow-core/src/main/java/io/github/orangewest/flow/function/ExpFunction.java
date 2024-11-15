package io.github.orangewest.flow.function;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import io.github.orangewest.flow.core.FlowContext;

/**
 * 表达式执行函数 exp(变量,表达式)
 */
public class ExpFunction implements OptFunction {

    @Override
    public String getName() {
        return "exp";
    }

    @Override
    public boolean execute(String[] params, FlowContext flowContext) {
        String variableName = params[0];
        String expressionStr = params[1];
        Expression expression = AviatorEvaluator.compile(expressionStr, true);
        Object execute = expression.execute(flowContext.getAllCalcVariables());
        flowContext.updateVariable(variableName, execute);
        return true;
    }

}
