package expression.generic.unary;

import expression.generic.GenericExpression;
import expression.generic.operations.GenericOperations;

import java.util.Objects;

public abstract class GenericUnaryOperation<T> implements GenericExpression<T> {
    protected final GenericExpression<T> node;

    protected GenericUnaryOperation(GenericExpression<T> node) {
        this.node = node;
    }

    protected abstract String getOperationString();

    protected abstract T compute(T a, GenericOperations<T> operations);

    @Override
    public T evaluate(T x, T y, T z, GenericOperations<T> operations) {
        return compute(node.evaluate(x, y, z, operations), operations);
    }

    @Override
    public String toString() {
        return getOperationString() + "(" + node + ')';
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
                obj instanceof GenericUnaryOperation<?> o
                        && Objects.equals(node, o.node)
                        && Objects.equals(getOperationString(), o.getOperationString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, getOperationString());
    }
}
