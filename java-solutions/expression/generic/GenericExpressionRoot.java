package expression.generic;

import expression.generic.operations.GenericOperations;

import java.util.Objects;

public class GenericExpressionRoot<T> {
    private final GenericOperations<T> operations;
    private final GenericExpression<T> expression;

    public GenericExpressionRoot(GenericExpression<T> expression, GenericOperations<T> operations) {
        this.operations = operations;
        this.expression = expression;
    }

    public T evaluate(T x, T y, T z) {
        return expression.evaluate(x, y, z, operations);
    }

    public T evaluate(int x, int y, int z) {
        return expression.evaluate(operations.valueOf(x), operations.valueOf(y), operations.valueOf(z), operations);
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(expression, obj);
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
