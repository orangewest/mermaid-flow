package io.github.orangewest.flow.node;

import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import io.github.orangewest.flow.aop.FlowAspectManager;
import io.github.orangewest.flow.aop.FlowNodeAspect;
import io.github.orangewest.flow.core.FlowContext;
import io.github.orangewest.flow.core.FlowNode;
import io.github.orangewest.flow.meta.FlowNodeMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 抽象流程节点类，实现FlowNode接口
 *
 * @author orangewest
 */
public abstract class AbstractFlowNode implements FlowNode {

    // 流程节点元数据
    protected FlowNodeMeta flowNodeMeta;

    // 下一个节点列表
    protected List<NextNode> nextNodes = new ArrayList<>();

    /**
     * 构造函数，初始化流程节点元数据
     *
     * @param flowNodeMeta 流程节点元数据
     */
    public AbstractFlowNode(FlowNodeMeta flowNodeMeta) {
        this.flowNodeMeta = flowNodeMeta;
        init();
    }

    /**
     * 初始化方法，子类可以覆盖此方法进行初始化操作
     */
    protected void init() {
    }

    /**
     * 执行流程节点
     *
     * @param flowContext 流程上下文
     */
    @Override
    public void execute(FlowContext flowContext) {
        // 执行前置切面
        List<FlowNodeAspect> flowNodeAspects = FlowAspectManager.getFlowNodeAspects();
        for (FlowNodeAspect flowNodeAspect : flowNodeAspects) {
            flowNodeAspect.beforeExecute(this, flowContext);
        }
        // 执行节点逻辑
        Object executeResult = execute0(flowContext);
        // 执行后置切面
        for (FlowNodeAspect flowNodeAspect : flowNodeAspects) {
            flowNodeAspect.afterExecute(this, flowContext);
        }
        // 执行下一个节点
        executeNextNode(executeResult, flowContext);
    }

    /**
     * 执行下一个节点
     *
     * @param executeResult 当前节点执行结果
     * @param flowContext   流程上下文
     */
    protected void executeNextNode(Object executeResult, FlowContext flowContext) {
        for (NextNode nextNode : this.nextNodes) {
            nextNode.execute(flowContext);
        }
    }

    /**
     * 添加下一个节点
     *
     * @param condition 条件表达式字符串
     * @param nextNode  下一个节点
     */
    public void addNextNode(String condition, AbstractFlowNode nextNode) {
        Expression expression = buildNextNodeExpression(condition);
        this.nextNodes.add(new NextNode(condition, expression, nextNode));
    }

    /**
     * 构建下一个节点的表达式
     *
     * @param condition 条件表达式字符串
     * @return 表达式对象
     */
    protected Expression buildNextNodeExpression(String condition) {
        if (StrUtil.isNotBlank(condition)) {
            return AviatorEvaluator.compile(condition, true);
        }
        return null;
    }

    /**
     * 获取流程节点元数据
     *
     * @return 流程节点元数据
     */
    public FlowNodeMeta getFlowNodeMeta() {
        return flowNodeMeta;
    }

    /**
     * 更新节点内容
     *
     * @param content 新的内容
     */
    public void updateContent(String content) {
        this.flowNodeMeta.setContent(content);
        init();
    }

    /**
     * 执行节点逻辑的抽象方法，由子类实现
     *
     * @param flowContext 流程上下文
     * @return 执行结果
     */
    protected abstract Object execute0(FlowContext flowContext);

    /**
     * 更新节点之间的链接条件
     *
     * @param nextNodeId 下一个节点ID
     * @param condition  新的条件表达式字符串
     */
    public void updateLink(String nextNodeId, String condition) {
        NextNode nextNode = this.nextNodes
                .stream()
                .filter(next -> Objects.equals(nextNodeId, next.getFlowNode().getFlowNodeMeta().getNodeId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("flowNodeId:" + nextNodeId + " not exists"));
        nextNode.updateCondition(condition, buildNextNodeExpression(condition));
    }

    /**
     * 下一个节点的内部类
     */
    protected static class NextNode {

        private Expression expression;
        private String condition;
        private final AbstractFlowNode flowNode;

        /**
         * 构造函数，初始化下一个节点
         *
         * @param condition  条件表达式字符串
         * @param expression 表达式对象
         * @param flowNode   下一个节点
         */
        public NextNode(String condition, Expression expression, AbstractFlowNode flowNode) {
            this.flowNode = flowNode;
            this.condition = condition;
            this.expression = expression;
        }

        public String getCondition() {
            return condition;
        }

        public AbstractFlowNode getFlowNode() {
            return flowNode;
        }

        /**
         * 执行下一个节点
         *
         * @param flowContext 流程上下文
         */
        public void execute(FlowContext flowContext) {
            flowNode.execute(flowContext);
        }

        public Expression getExpression() {
            return expression;
        }

        /**
         * 更新条件表达式
         *
         * @param condition  条件表达式字符串
         * @param expression 表达式对象
         */
        public void updateCondition(String condition, Expression expression) {
            this.condition = condition;
            this.expression = expression;
        }

    }


}
