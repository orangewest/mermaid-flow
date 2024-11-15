package io.github.orangewest.flow.core;

import io.github.orangewest.flow.variable.VariableKey;
import io.github.orangewest.flow.variable.VariableRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 流程上下文
 *
 * @author orangewest
 */
public class FlowContext {

    /**
     * 上下文ID
     */
    private String contextId;

    /**
     * 执行次数
     */
    private AtomicInteger count;

    /**
     * 全局变量仓库
     */
    private final VariableRepository globalVariableRepository = new VariableRepository();

    /**
     * 局部变量仓库
     */
    private final VariableRepository localVariableRepository = new VariableRepository();

    public String getContextId() {
        return contextId;
    }

    public int getCount() {
        return count.get();
    }

    public FlowContext() {
        init();
    }

    public void addCount() {
        this.count.incrementAndGet();
    }

    public void destroy() {
        this.clear();
    }

    private void init() {
        this.contextId = String.valueOf(System.currentTimeMillis());
        this.count = new AtomicInteger(0);
    }

    /**
     * 初始化全局变量仓库
     */
    public void initGlobalVariable(Map<VariableKey<?>, Object> variableMap) {
        globalVariableRepository.init(variableMap);
    }

    /**
     * 清空变量仓库
     */
    public void clear() {
        globalVariableRepository.clear();
        localVariableRepository.clear();
    }

    /**
     * 重置局部变量
     */
    public void reset() {
        localVariableRepository.clear();
    }

    /**
     * 获取变量值
     *
     * @param variableKey 变量key
     * @param <T>         变量类型
     * @return 变量值
     */
    public <T> T get(VariableKey<T> variableKey) {
        return getOrDefault(variableKey, null);
    }

    /**
     * 获取变量值，优先从局部变量仓库获取，如果没有则从全局变量仓库获取
     *
     * @param variableKey 变量key
     * @param <T>         变量类型
     * @return 变量值
     */
    public <T> T getOrDefault(VariableKey<T> variableKey, T defaultValue) {
        return localVariableRepository.getOrDefault(variableKey, globalVariableRepository.getOrDefault(variableKey, defaultValue));
    }

    /**
     * 获取变量值，只从局部变量仓库获取
     *
     * @param variableKey 变量key
     * @param <T>         变量类型
     * @return 变量值
     */
    public <T> T getOrDefaultFromLocal(VariableKey<T> variableKey, T defaultValue) {
        return localVariableRepository.getOrDefault(variableKey, defaultValue);
    }

    /**
     * 获取变量值，只从局部变量仓库获取
     *
     * @param variableKey 变量key
     * @param <T>         变量类型
     * @return 变量值
     */
    public <T> T getFromLocal(VariableKey<T> variableKey) {
        return getOrDefaultFromLocal(variableKey, null);
    }


    /**
     * 获取变量值，只从全局变量仓库获取
     *
     * @param variableKey 变量key
     * @param <T>         变量类型
     * @return 变量值
     */
    public <T> T getOrDefaultFromGlobal(VariableKey<T> variableKey, T defaultValue) {
        return globalVariableRepository.getOrDefault(variableKey, defaultValue);
    }

    /**
     * 获取变量值，只从全局变量仓库获取
     *
     * @param variableKey 变量key
     * @param <T>         变量类型
     * @return 变量值
     */
    public <T> T getFromGlobal(VariableKey<T> variableKey) {
        return getOrDefaultFromGlobal(variableKey, null);
    }

    /**
     * 设置变量值，如果全局变量仓库中存在该变量，则设置到全局变量仓库，否则设置到局部变量仓库
     *
     * @param variableKey 变量key
     * @param value       变量值
     * @param <T>         变量类型
     */
    public <T> void put(VariableKey<T> variableKey, T value) {
        if (globalVariableRepository.contains(variableKey)) {
            putGlobal(variableKey, value);
        } else {
            putLocal(variableKey, value);
        }
    }

    /**
     * 设置变量值，只设置到全局变量仓库
     *
     * @param variableKey 变量key
     * @param value       变量值
     * @param <T>         变量类型
     */
    public <T> void putGlobal(VariableKey<T> variableKey, T value) {
        globalVariableRepository.put(variableKey, value);
    }

    /**
     * 设置变量值，只设置到局部变量仓库
     *
     * @param variableKey 变量key
     * @param value       变量值
     * @param <T>         变量类型
     */
    public <T> void putLocal(VariableKey<T> variableKey, T value) {
        localVariableRepository.put(variableKey, value);
    }

    /**
     * 获取所有计算变量值，包含全局变量和局部变量
     *
     * @return 全部计算变量值
     */
    public Map<String, Object> getAllCalcVariables() {
        Map<String, Object> allVariables = new HashMap<>();
        Map<String, Object> allGlobal = globalVariableRepository.getAllCalc();
        Map<String, Object> allLocal = localVariableRepository.getAllCalc();
        allVariables.putAll(allGlobal);
        allVariables.putAll(allLocal);
        return allVariables;
    }

    /**
     * 获取所有存储变量值，只包含全局变量
     *
     * @return 全部存储变量值
     */
    public Map<String, Object> getAllStoreVariables() {
        return globalVariableRepository.getAllStore();
    }

    /**
     * 更新变量值，如果全局变量仓库中存在该变量，则更新全局变量值，否则创建局部变量
     *
     * @param name  变量名
     * @param value 变量值
     */
    public void updateVariable(String name, Object value) {
        VariableKey<?> variableKey = globalVariableRepository.findVariableKey(name);
        if (variableKey != null) {
            globalVariableRepository.put(variableKey, value);
        } else {
            variableKey = localVariableRepository.findVariableKey(name);
            if (variableKey == null) {
                variableKey = VariableKey.of(name, value.getClass());
            }
            localVariableRepository.put(variableKey, value);
        }
    }


}
