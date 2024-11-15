package io.github.orangewest.flow.aop;

import io.github.orangewest.flow.core.Flow;
import io.github.orangewest.flow.core.FlowContext;

/**
 * 流程切面
 *
 * @author orangewest
 */
public interface FlowAspect {

    /**
     * 流程启动前切面
     *
     * @param flow        flow
     * @param flowContext flowContext
     */
    default void beforeStart(Flow flow, FlowContext flowContext) {

    }

    /**
     * 流程启动后切面
     *
     * @param flow        flow
     * @param flowContext flowContext
     */
    default void afterStart(Flow flow, FlowContext flowContext) {

    }

}
