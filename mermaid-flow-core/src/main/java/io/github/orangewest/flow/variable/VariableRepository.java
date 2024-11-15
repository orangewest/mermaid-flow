package io.github.orangewest.flow.variable;

import cn.hutool.core.convert.Convert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VariableRepository {

    private final Map<VariableKey<?>, VariableValue> variableMap = new ConcurrentHashMap<>();

    public boolean contains(VariableKey<?> variableKey) {
        return variableMap.containsKey(variableKey);
    }

    public void put(VariableKey<?> variableKey, Object value) {
        if (!Objects.equals(variableKey.getType(), value.getClass())) {
            value = Convert.convert(variableKey.getType(), value);
        }
        variableMap.put(variableKey, VariableValue.of(value));
    }

    public <T> T get(VariableKey<T> variableKey) {
        return getOrDefault(variableKey, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(VariableKey<T> variableKey, T defaultValue) {
        return (T) Optional.ofNullable(variableMap.get(variableKey))
                .map(VariableValue::getVal)
                .orElse(defaultValue);
    }


    public Map<String, Object> getAllCalc() {
        Map<String, Object> result = new HashMap<>();
        variableMap.forEach((key, value) -> {
            Object val = value.getVal();
            if (val instanceof Number) {
                result.put(key.getName(), val);
            }
        });
        return result;
    }

    public Map<String, Object> getAllStore() {
        Map<String, Object> result = new HashMap<>();
        variableMap.forEach((key, value) -> {
            if (key.isStore()) {
                result.put(key.getName(), value.getVal());
            }
        });
        return result;
    }

    public void clear() {
        variableMap.clear();
    }

    public void init(Map<VariableKey<?>, Object> variableMap) {
        variableMap.forEach(this::put);
    }

    public VariableKey<?> findVariableKey(String name) {
        return findVariableKey(variableMap.keySet(), name);
    }

    private VariableKey<?> findVariableKey(Set<VariableKey<?>> variableKeys, String name) {
        return variableKeys.stream()
                .filter(key -> key.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
