package io.github.orangewest.flow.variable;


import java.lang.reflect.Type;
import java.util.Objects;

public class VariableKey<T> {

    private final String name;

    private final Type type;

    private final boolean store;

    private VariableKey(String name, Type type, boolean store) {
        this.name = name;
        this.type = type;
        this.store = store;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isStore() {
        return store;
    }

    public static <T> VariableKey<T> of(String name, Class<T> type) {
        return new VariableKey<>(name, type, false);
    }

    public static <T> VariableKey<T> of(String name, TypeReference<T> type) {
        return new VariableKey<>(name, type.getType(), false);
    }

    public static <T> VariableKey<T> of(String name, Class<T> type, boolean store) {
        return new VariableKey<>(name, type, store);
    }

    public static <T> VariableKey<T> of(String name, TypeReference<T> type, boolean store) {
        return new VariableKey<>(name, type.getType(), store);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableKey<?> that = (VariableKey<?>) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
