package expression.generic;

import expression.generic.operations.GenericOperations;

import java.util.Objects;

public class GenericConst<T> implements GenericExpression<T> {
    private final T c;
    public GenericConst(T c) {
        this.c = c;
    }
    @Override
    public T evaluate(T x, T y, T z, GenericOperations<T> operations) {
        return c;
    }

    @Override
    public String toString() {
        return c.toString();
    }

    @Override
    public int hashCode() {
        return c.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
                obj instanceof GenericConst<?> o && Objects.equals(c, o.c);
    }
}
