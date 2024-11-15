package io.github.orangewest.flow.spring;

import io.github.orangewest.flow.aop.FlowAspect;
import io.github.orangewest.flow.aop.FlowAspectManager;
import io.github.orangewest.flow.aop.FlowNodeAspect;
import io.github.orangewest.flow.function.OptFunction;
import io.github.orangewest.flow.function.OptFunctionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MermaidFlowAutoConfiguration implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof FlowAspect) {
            FlowAspectManager.addFlowAspect((FlowAspect) bean);
        } else if (bean instanceof FlowNodeAspect) {
            FlowAspectManager.addFlowNodeAspect((FlowNodeAspect) bean);
        } else if (bean instanceof OptFunction) {
            OptFunctionFactory.register((OptFunction) bean);
        }
        return bean;
    }

}
