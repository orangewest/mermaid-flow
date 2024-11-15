package io.github.orangewest.flow.function;

import io.github.orangewest.flow.core.FlowContext;

/**
 * OptFunction接口定义了操作函数的标准，允许执行特定操作并返回执行结果。
 * 这个接口主要用于在特定的流程上下文中执行一些操作，如数据处理、流程控制等。
 *
 * @author orangewest
 */
public interface OptFunction {

    /**
     * 获取操作函数的名称。
     *
     * @return 操作函数的名称。
     */
    String getName();

    /**
     * 执行操作函数。
     *
     * @param params      执行函数所需的参数数组。
     * @param flowContext 流程上下文，包含执行环境的相关信息。
     * @return 执行结果，true表示执行成功，false表示执行失败。
     */
    boolean execute(String[] params, FlowContext flowContext);
}
