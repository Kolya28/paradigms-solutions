package expression.generic.binary;

import expression.generic.GenericExpression;
import expression.generic.operations.GenericOperations;

import java.util.Objects;

public abstract class GenericBinaryOperation<T> implements GenericExpression<T> {
    protected final GenericExpression<T> left, right;

    protected GenericBinaryOperation(GenericExpression<T> left, GenericExpression<T> right) {
        this.left = left;
        this.right = right;
    }

    protected abstract String getOperationString();

    protected abstract T compute(T a, T b, GenericOperations<T> operations);

    @Override
    public T evaluate(T x, T y, T z, GenericOperations<T> operations) {
        var leftResult = left.evaluate(x, y, z, operations);
        var rightResult = right.evaluate(x, y, z, operations);
        return compute(leftResult, rightResult, operations);
    }

    @Override
    public String toString() {
        return "(" + left + ' ' + getOperationString() + ' ' + right + ')';
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
            obj instanceof GenericBinaryOperation<?> o
                && Objects.equals(left, o.left) && Objects.equals(right, o.right)
                && Objects.equals(getOperationString(), o.getOperationString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, getOperationString(), right);
    }
}
