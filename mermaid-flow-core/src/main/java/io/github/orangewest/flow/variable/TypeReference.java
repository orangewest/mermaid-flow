package io.github.orangewest.flow.variable;

import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;

public abstract class TypeReference<T> implements Type {

    /**
     * 泛型参数
     */
    private final Type type;

    /**
     * 构造
     */
    public TypeReference() {
        this.type = TypeUtil.getTypeArgument(getClass());
    }

    /**
     * 获取用户定义的泛型参数
     *
     * @return 泛型参数
     */
    public Type getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

}
