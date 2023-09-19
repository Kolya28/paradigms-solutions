package expression.generic;

import expression.generic.operations.GenericOperations;

import java.util.Objects;

public class GenericVariable<T> implements GenericExpression<T> {
    private final String name;

    public GenericVariable(String name) {
        this.name = name;
    }

    @Override
    public T evaluate(T x, T y, T z, GenericOperations<T> operations) {
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
                obj instanceof GenericVariable<?> o && Objects.equals(o.name, name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
