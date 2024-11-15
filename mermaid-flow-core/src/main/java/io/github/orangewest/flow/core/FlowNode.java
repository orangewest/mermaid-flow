package io.github.orangewest.flow.core;

/**
 * FlowNode接口代表工作流中的一个节点它定义了节点在执行时需要完成的操作
 *
 * @author orangewest
 */
public interface FlowNode {

    /**
     * 执行当前流程节点的操作
     *
     * @param flowContext 流程上下文，包含了流程执行所需的数据和环境信息
     *                    通过这个参数，节点可以访问和修改流程中的共享数据
     *                    或者根据需要改变流程的执行路径
     */
    void execute(FlowContext flowContext);

}
