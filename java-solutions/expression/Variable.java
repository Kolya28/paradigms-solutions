package expression;

import java.util.Objects;

public class Variable implements AnyExpression {
    private final String name;

    @Override
    public String toMiniString() {
        return AnyExpression.super.toMiniString();
    }

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new ArithmeticException("Unknown variable name: " + name);
        };
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
                obj instanceof Variable o && Objects.equals(o.name, name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
