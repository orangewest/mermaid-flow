package io.github.orangewest.flow.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程元数据
 *
 * @author orangewest
 */
public class FlowMeta {

    /**
     * 节点元数据
     */
    private final List<FlowNodeMeta> nodeMetaList = new ArrayList<>();

    /**
     * 连接元数据
     */
    private final List<FlowLinkMeta> linkMetaList = new ArrayList<>();

    public void addNodeMeta(FlowNodeMeta nodeMeta) {
        nodeMetaList.add(nodeMeta);
    }

    public void addLinkMeta(FlowLinkMeta linkMeta) {
        linkMetaList.add(linkMeta);
    }

    public List<FlowNodeMeta> getNodeMetaList() {
        return nodeMetaList;
    }

    public List<FlowLinkMeta> getLinkMetaList() {
        return linkMetaList;
    }

}
