package io.github.orangewest.flow.core;

import io.github.orangewest.flow.meta.FlowMeta;
import io.github.orangewest.flow.parser.FlowchartParser;
import io.github.orangewest.flow.variable.VariableKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程工厂类，用于创建和管理流程实例
 *
 * @author orangewest
 */
public class FlowFactory {

    // 流程图解析器，用于解析流程图定义
    private FlowchartParser flowchartParser;

    // 全局变量映射，用于存储和管理流程中使用的全局变量
    private final Map<VariableKey<?>, Object> globalVariableMap = new ConcurrentHashMap<>();

    // 流程映射，用于存储和管理已创建的流程实例
    private final Map<String, Flow> flowMap = new ConcurrentHashMap<>();

    /**
     * 设置流程图解析器
     *
     * @param flowchartParser 流程图解析器实例
     */
    public void setFlowchartParser(FlowchartParser flowchartParser) {
        this.flowchartParser = flowchartParser;
    }

    /**
     * 初始化计算变量，通常用于批量设置全局变量
     *
     * @param globalVariableMap 全局变量映射
     */
    public void initVariable(Map<VariableKey<?>, Object> globalVariableMap) {
        this.globalVariableMap.putAll(globalVariableMap);
    }

    /**
     * 初始化单个变量，用于在流程中设置初始变量值
     *
     * @param variableKey 变量键
     * @param value       变量值
     * @param <T>         变量类型，限定为Number的子类
     */
    public <T extends Number> void initVariable(VariableKey<T> variableKey, T value) {
        this.globalVariableMap.put(variableKey, value);
    }

    /**
     * 获取流程实例，如果不存在则创建
     *
     * @param flowId 流程标识符
     * @return 返回已存在的或新创建的流程实例
     */
    public Flow getOrCreateFlow(String flowId) {
        return flowMap.computeIfAbsent(flowId, key -> {
            // 解析流程图定义
            FlowMeta flowMeta = flowchartParser.parse();
            // 创建流程实例
            Flow flow = new Flow(flowId, flowMeta);
            // 初始化流程上下文，并传入全局变量
            FlowContext flowContext = new FlowContext();
            flowContext.initGlobalVariable(globalVariableMap);
            flow.setFlowContext(flowContext);
            // 初始化流程
            flow.init();
            return flow;
        });
    }

    /**
     * 销毁流程
     *
     * @param flowId 流程标识符
     */
    public void destroyFlow(String flowId) {
        Flow flow = flowMap.remove(flowId);
        if (flow != null) {
            // 执行流程销毁逻辑
            flow.destroy();
        }
    }

}
