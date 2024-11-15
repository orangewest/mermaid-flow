package io.github.orangewest.flow.exception;

public class FlowNodeNotExistsException extends RuntimeException {

    private static final long serialVersionUID = -4934344124897680738L;

    public FlowNodeNotExistsException(String flowId, String nodeId) {
        super("flowId:" + flowId + " nodeId:" + nodeId + " not exists");
    }

}
