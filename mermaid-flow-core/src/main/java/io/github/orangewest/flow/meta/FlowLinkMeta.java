package io.github.orangewest.flow.meta;

/**
 * 节点连线元数据
 *
 * @author orangewest
 */
public class FlowLinkMeta {

    /**
     * 源节点id
     */
    private final String fromNodeId;

    /**
     * 目标节点id
     */
    private final String toNodeId;

    /**
     * 连接条件
     */
    private final String condition;


    public FlowLinkMeta(String fromNodeId, String toNodeId, String condition) {
        this.fromNodeId = fromNodeId.trim();
        this.toNodeId = toNodeId.trim();
        this.condition = condition.trim().replace("\"", "");
    }

    public FlowLinkMeta(String fromNodeId, String toNodeId) {
        this(fromNodeId, toNodeId, "");
    }

    public String getFromNodeId() {
        return fromNodeId;
    }

    public String getToNodeId() {
        return toNodeId;
    }

    public String getCondition() {
        return condition;
    }

}
