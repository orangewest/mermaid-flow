package io.github.orangewest.flow.meta;

import io.github.orangewest.flow.enums.FlowNodeType;

import java.util.Objects;

/**
 * 节点元数据
 */
public class FlowNodeMeta {

    private final FlowNodeType nodeType;

    private final String nodeId;

    private String content;

    public FlowNodeMeta(FlowNodeType nodeType, String nodeId, String content) {
        this.nodeType = nodeType;
        this.nodeId = nodeId.trim();
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FlowNodeType getNodeType() {
        return nodeType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getContent() {
        return this.content.trim().replace("\"", "");
    }

    public static FlowNodeMeta buildOptNode(String nodeId, String content) {
        if (Objects.equals(nodeId, "start")) {
            return new FlowNodeMeta(FlowNodeType.START, nodeId, content);
        }
        return new FlowNodeMeta(FlowNodeType.OPT, nodeId, content);
    }

    public static FlowNodeMeta buildCondNode(String nodeId, String content) {
        return new FlowNodeMeta(FlowNodeType.COND, nodeId, content);
    }

}
