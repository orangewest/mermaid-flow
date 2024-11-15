package io.github.orangewest.flow.function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OptFunctionFactory类用于管理和创建OptFunction实例
 * 它提供了一种方式来注册和获取函数对象，以便在其他部分中重用和调用这些函数
 *
 * @author orangewest
 */
public class OptFunctionFactory {

    // 存储函数名与对应OptFunction实例的映射
    private final static Map<String, OptFunction> FUNCTION_MAP = new ConcurrentHashMap<>();

    // 初始化预定义的函数
    static {
        register(new ExpFunction());
    }

    /**
     * 注册一个新的OptFunction实例到函数映射中
     *
     * @param optFunction 要注册的OptFunction实例，它将被添加到函数映射中
     */
    public static void register(OptFunction optFunction) {
        FUNCTION_MAP.put(optFunction.getName(), optFunction);
    }

    /**
     * 根据函数名获取对应的OptFunction实例
     *
     * @param name 函数的名称，作为获取OptFunction实例的关键字
     * @return 返回对应的OptFunction实例，如果不存在则返回null
     */
    public static OptFunction getFunction(String name) {
        return FUNCTION_MAP.get(name);
    }

}
