package io.github.orangewest.flow.parser;

import io.github.orangewest.flow.meta.FlowMeta;

public interface FlowchartParser {

    /**
     * 解析流程图
     *
     * @return 流程图元数据
     */
    FlowMeta parse();

}
