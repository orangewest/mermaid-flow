package io.github.orangewest.flow.parser;

import cn.hutool.core.collection.CollectionUtil;
import io.github.orangewest.flow.exception.FlowParseException;
import io.github.orangewest.flow.meta.FlowLinkMeta;
import io.github.orangewest.flow.meta.FlowMeta;
import io.github.orangewest.flow.meta.FlowNodeMeta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MermaidFileParser implements FlowchartParser {

    private final String mermaidFilePath;

    /**
     * 匹配mermaid中所有声明的操作节点信息
     */
    private static final String OPT_NODE_REGEX = "(\\w+)\\[(.*?)]";

    private static final Pattern OPT_NODE_PATTERN = Pattern.compile(OPT_NODE_REGEX);

    /**
     * 匹配mermaid中所有声明的操作节点信息
     */
    private static final String COND_NODE_REGEX = "(\\w+)\\{(.*?)}";

    private static final Pattern COND_NODE_PATTERN = Pattern.compile(COND_NODE_REGEX);

    /**
     * 匹配mermaid中的链接关系
     */
    private static final String LINK_REGEX = "(\\w+)((-->|--.*-->)(\\w+))+";

    private static final Pattern LINK_PATTERN = Pattern.compile(LINK_REGEX);


    public MermaidFileParser(String mermaidFilePath) {
        this.mermaidFilePath = mermaidFilePath;
    }

    @Override
    public FlowMeta parse() {
        FlowMeta flowMeta = new FlowMeta();
        try (Stream<String> lines = Files.lines(Paths.get(this.mermaidFilePath))) {
            lines.forEach(line -> parseLine(line, flowMeta));
        } catch (IOException e) {
            throw new FlowParseException(MessageFormat.format("文件路径【{0}】解析失败！", this.mermaidFilePath), e);
        }
        checkFlowMeta(flowMeta);
        return flowMeta;
    }

    private void parseLine(String line, FlowMeta flowMeta) {
        try {
            if (line == null || line.trim().length() == 0) {
                return;
            }
            Matcher matcher = OPT_NODE_PATTERN.matcher(line);
            if (matcher.find()) {
                flowMeta.addNodeMeta(FlowNodeMeta.buildOptNode(matcher.group(1), matcher.group(2)));
                return;
            }
            matcher = COND_NODE_PATTERN.matcher(line);
            if (matcher.find()) {
                flowMeta.addNodeMeta(FlowNodeMeta.buildCondNode(matcher.group(1), matcher.group(2)));
                return;
            }
            matcher = LINK_PATTERN.matcher(line);
            if (matcher.find()) {
                String[] split = line.split("-->");
                String[] fromNodeArr = null;
                for (int i = 0; i < split.length; i++) {
                    String[] toNodeArr = split[i].split("--");
                    if (i > 0) {
                        String fromNodeId = fromNodeArr[0];
                        String toNodeId = toNodeArr[0];
                        if (fromNodeArr.length == 2) {
                            flowMeta.addLinkMeta(new FlowLinkMeta(fromNodeId, toNodeId, fromNodeArr[1]));
                        } else {
                            flowMeta.addLinkMeta(new FlowLinkMeta(fromNodeId, toNodeId));
                        }
                    }
                    fromNodeArr = toNodeArr;
                }
            }
        } catch (Exception e) {
            throw new FlowParseException(MessageFormat.format("文本【{0}】解析失败！", line), e);
        }

    }

    private void checkFlowMeta(FlowMeta flowMeta) {
        if (CollectionUtil.isEmpty(flowMeta.getNodeMetaList())) {
            throw new FlowParseException("流程图节点为空！");
        }
        if (CollectionUtil.isEmpty(flowMeta.getLinkMetaList())) {
            throw new FlowParseException("流程图链接为空！");
        }
    }

}
