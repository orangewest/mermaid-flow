package io.github.orangewest.flow.core;

import io.github.orangewest.flow.aop.FlowAspectManager;
import io.github.orangewest.flow.aop.FlowExecAop;
import io.github.orangewest.flow.aop.FlowNodeExecAop;
import io.github.orangewest.flow.parser.MermaidFileParser;
import io.github.orangewest.flow.variable.VariableKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FlowFactoryTest {

    @BeforeAll
    static void before() {
        FlowAspectManager.addFlowNodeAspect(new FlowNodeExecAop());
        FlowAspectManager.addFlowAspect(new FlowExecAop());
    }

    @Test
    void testFlow1() {
        // 创建一个流程工厂实例
        FlowFactory flowFactory = new FlowFactory();
        // 设置流程图解析器，使用Mermaid文件格式的流程图
        flowFactory.setFlowchartParser(new MermaidFileParser(getFilePath("flow1.md")));
        // 定义一个整型变量键，用于流程中变量的访问
        VariableKey<Integer> x = VariableKey.of("x", Integer.class);
        // 初始化流程变量x为0
        flowFactory.initVariable(x, 0);
        // 获取或创建一个名为"flow"的流程实例
        Flow flow = flowFactory.getOrCreateFlow("flow");
        // 获取流程的上下文，用于存储和访问流程执行过程中的变量
        FlowContext flowContext = flow.getFlowContext();
        // 循环10次，每次执行流程，并验证流程的执行结果
        for (int j = 1; j <= 10; j++) {
            // 定义一个局部变量键i，用于循环中的临时变量
            VariableKey<Integer> i = VariableKey.of("i", Integer.class);
            // 在流程上下文中设置局部变量i为0
            flowContext.putLocal(i, 0);
            // 启动流程执行
            flow.start();
            // 验证流程执行次数是否与预期相符
            Assertions.assertEquals(j, flowContext.getCount());
            // 验证流程中变量x的值是否与预期相符
            Assertions.assertEquals(j * 1000, flowContext.get(x));
        }
    }

    @Test
    void testFlow2() {
        FlowFactory flowFactory = new FlowFactory();
        flowFactory.setFlowchartParser(new MermaidFileParser(getFilePath("flow2.md")));
        VariableKey<Integer> x = VariableKey.of("x", Integer.class);
        VariableKey<Integer> y = VariableKey.of("y", Integer.class);
        flowFactory.initVariable(x, 0);
        flowFactory.initVariable(y, 0);
        Flow flow = flowFactory.getOrCreateFlow("flow");
        flow.start();
        FlowContext flowContext = flow.getFlowContext();
        Assertions.assertEquals(1, flowContext.get(x));
        Assertions.assertEquals(2, flowContext.get(y));
    }

    @Test
    void testFlow3() {
        FlowFactory flowFactory = new FlowFactory();
        flowFactory.setFlowchartParser(new MermaidFileParser(getFilePath("flow3.md")));
        VariableKey<Integer> x = VariableKey.of("x", Integer.class);
        VariableKey<Integer> y = VariableKey.of("y", Integer.class);
        flowFactory.initVariable(x, 5);
        flowFactory.initVariable(y, 0);
        Flow flow = flowFactory.getOrCreateFlow("flow");
        FlowContext flowContext = flow.getFlowContext();
        for (int i = 0; i < 10; i++) {
            System.out.println("x:" + flowContext.get(x) + " y:" + flowContext.get(y));
            flow.start();
        }
    }

    @Test
    void testFlow4() {
        FlowFactory flowFactory = new FlowFactory();
        flowFactory.setFlowchartParser(new MermaidFileParser(getFilePath("flow4.md")));
        VariableKey<Integer> x = VariableKey.of("x", Integer.class);
        VariableKey<Integer> y = VariableKey.of("y", Integer.class);
        flowFactory.initVariable(x, 5);
        flowFactory.initVariable(y, 0);
        Flow flow = flowFactory.getOrCreateFlow("flow");
        FlowContext flowContext = flow.getFlowContext();
        for (int i = 0; i < 10; i++) {
            System.out.println("x:" + flowContext.get(x) + " y:" + flowContext.get(y));
            flow.start();
        }
    }

    @Test
    void testFlow5() {
        FlowFactory flowFactory = new FlowFactory();
        flowFactory.setFlowchartParser(new MermaidFileParser(getFilePath("flow5.md")));
        VariableKey<Integer> x = VariableKey.of("x", Integer.class);
        VariableKey<Integer> y = VariableKey.of("y", Integer.class);
        flowFactory.initVariable(x, 1);
        flowFactory.initVariable(y, 0);
        Flow flow = flowFactory.getOrCreateFlow("flow");
        FlowContext flowContext = flow.getFlowContext();
        for (int i = 0; i < 10; i++) {
            System.out.println("x:" + flowContext.get(x) + " y:" + flowContext.get(y));
            flow.start();
        }
    }

    @Test
    void testFlow4Update() {
        FlowFactory flowFactory = new FlowFactory();
        flowFactory.setFlowchartParser(new MermaidFileParser(getFilePath("flow4.md")));
        VariableKey<Integer> x = VariableKey.of("x", Integer.class);
        VariableKey<Integer> y = VariableKey.of("y", Integer.class);
        flowFactory.initVariable(x, 5);
        flowFactory.initVariable(y, 0);
        Flow flow = flowFactory.getOrCreateFlow("flow");
        FlowContext flowContext = flow.getFlowContext();
        for (int i = 0; i < 10; i++) {
            if (i == 6) {
                flow.updateNodeContent("12000", "x=x+5");
            }
            if (i == 8) {
                flow.updateNodeLink("10000", "12000", "y>5");
            }
            System.out.println("x:" + flowContext.get(x) + " y:" + flowContext.get(y));
            flow.start();
        }
    }


    private String getFilePath(String fileName) {
        String filePath = this.getClass().getClassLoader().getResource(fileName).getFile();
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        return filePath;
    }

}