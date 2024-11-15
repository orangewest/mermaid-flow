package io.github.orangewest.flow.variable;

public class VariableValue {

    private final Object val;

    private VariableValue(Object val) {
        this.val = val;
    }

    public static VariableValue of(Object val) {
        return new VariableValue(val);
    }

    public Object getVal() {
        return val;
    }


}
